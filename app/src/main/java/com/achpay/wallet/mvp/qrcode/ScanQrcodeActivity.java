package com.achpay.wallet.mvp.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.model.OrderCreateRequest;
import com.achpay.wallet.model.OrderCreateResponse;
import com.achpay.wallet.model.OrderStatusMessage;
import com.achpay.wallet.model.RaidenPayResponse;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.order.OrderDetailActivity;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.service.MessageService;
import com.achpay.wallet.service.ProtectService;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.web3j.crypto.WalletUtils;

import java.util.ArrayList;
import java.util.Arrays;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanQrcodeActivity extends Activity implements QRCodeView.Delegate {

    private String currency_name;
    private String currency_id;
    private String coin_name;
    private String coinId;
    private String currency_amount;
    private String userAddress;
    private KProgressHUD progressHUD;
    private String mOrderId;

//    private ZBarView mScanView;
    private ZXingView mScanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        mScanView = findViewById(R.id.qr_code_view);
        mScanView.setDelegate(this);

        currency_name = getIntent().getStringExtra(TransParams.CURRENCY_NAME);
        currency_id = getIntent().getStringExtra(TransParams.CURRENCY_ID);
        coin_name = getIntent().getStringExtra(TransParams.COIN_NAME);
        coinId = getIntent().getStringExtra(TransParams.COIN_ID);
        currency_amount = getIntent().getStringExtra(TransParams.AMOUNT);

        EventBusUtil.register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mScanView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别

        mScanView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
    }

    @Override
    protected void onStop() {
        mScanView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        mScanView.showScanRect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanView.onDestroy(); // 销毁二维码扫描控件
        EventBusUtil.unregister(this);
    }


    @Override
    public void onScanQRCodeSuccess(String result) {

        userAddress = parseAddress(result);

        if (!WalletUtils.isValidAddress(userAddress)) {
            Toast.makeText(this, "InValid Address!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        mScanView.stopCamera();
        mScanView.showScanRect();

        showProgress();

        createOrder(this, CommonUtil.createOrderId());
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.i("error");
    }

    public void showProgress() {
        if (progressHUD == null) {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setSize(125, 125)
                    .setLabel(getString(R.string.progress_wait))
                    .setCancellable(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressHUD.dismiss();
                            ScanQrcodeActivity.this.finish();
                        }
                    });
        }

        if (!progressHUD.isShowing()) {
            progressHUD.show();
        }
    }

    public void dismissProgress() {
        progressHUD.dismiss();
    }


    public void createOrder(final Context mContext, String orderId) {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setOrderId(orderId);
        request.setOrderQuantityFait(currency_amount);
        request.setCryptoCurrencyId(coinId);
        request.setCurrencyId(CommonUtil.getCurrencyId());
        request.setTimezone(CommonUtil.getTimeZone());
        request.setSign("ACHPAY");
        request.setAddressType(TransParams.ADDRESS_NORMAL);
        request.setIsSales("N");

        RetrofitUtil.getInstance().createOrder(request, new BaseSubscriber<UniteResponse<OrderCreateResponse>>() {

            @Override
            public void onResponse(UniteResponse<OrderCreateResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    OrderCreateResponse orderCreateResponse = response.getData();

                    //创建订单成功以后,检查websocket连接
                    if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.MessageService")) {
                        Intent intent = new Intent(mContext, MessageService.class);
                        mContext.startService(intent);
                    }

                    if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.ProtectService")) {
                        Intent intent = new Intent(mContext, ProtectService.class);
                        mContext.startService(intent);
                    }

                    WSClientMessage message = new WSClientMessage();
                    message.setMessage(TransParams.CHECK_WEBSOCKET);
                    EventBusUtil.post(message);

                    String url = orderCreateResponse.getAddress();

                    mOrderId = orderCreateResponse.getOrderId();

                    RetrofitUtil.getInstance().raidenPay(url.concat(userAddress), new BaseSubscriber<RaidenPayResponse>() {
                        @Override
                        public void onResponse(RaidenPayResponse raidenPayResponse) {
                            Log.i(GsonUtil.objectToJson(raidenPayResponse));

                            if (raidenPayResponse.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                                //扣款成功
                                AppUtil.closeActivityOnSchedule(ScanQrcodeActivity.this, 15L);
                                /*
                                Intent intent = new Intent(ScanQrcodeActivity.this, OrderDetailActivity.class);
                                intent.putExtra(User.ORDER_ID, orderId);
                                startActivity(intent);
                                */
                            } else {
                                endPay(raidenPayResponse.getReturnMsg(), true);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            endPay(getString(R.string.deduct_currency_failed), true);
                        }
                    });
                } else {
                    //订单创建失败
                    endPay(getString(R.string.order_create_failed), true);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                endPay(getString(R.string.order_create_failed), true);
            }
        });
    }

    private String parseAddress(String result) {

        int nameSplit = result.indexOf(":");

        if (nameSplit == -1) {

            return result;
        }

        String title = result.substring(0, nameSplit);

        String bitcoinPaymentURIWithoutScheme = result.replaceFirst(title + ":", "");

        ArrayList<String> bitcoinPaymentURIElements = new ArrayList<>(Arrays.asList(bitcoinPaymentURIWithoutScheme.split("\\?")));

        if (bitcoinPaymentURIElements.size() != 1 && bitcoinPaymentURIElements.size() != 2) {
            return result;
        } else if (bitcoinPaymentURIElements.get(0).length() == 0) {
            return result;
        } else {
            return bitcoinPaymentURIElements.get(0);
        }
    }

    private void endPay(String messge, boolean finishActivity) {
        Toast.makeText(ScanQrcodeActivity.this, messge, Toast.LENGTH_SHORT).show();
        dismissProgress();
        if (finishActivity) {
            ScanQrcodeActivity.this.finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatusMessageReceived(OrderStatusMessage message) {
        //收到最新的一笔订单推送
        if (this.mOrderId.equals(message.getOrderId())) {
            /*if (message.getResult().equals(ResponseParam.SUCCESS)
                    || message.getResult().equals(ResponseParam.MSUCCESS)
                    || message.getResult().equals(ResponseParam.LSUCCESS)) {
                Toast.makeText(this, getString(R.string.receipt_order_success), Toast.LENGTH_SHORT).show();
                this.finish();
            }*/
            endPay(getString(R.string.deduct_currency_success), false);
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(User.ORDER_ID, mOrderId);
            startActivity(intent);
            this.finish();
        }
    }
}
