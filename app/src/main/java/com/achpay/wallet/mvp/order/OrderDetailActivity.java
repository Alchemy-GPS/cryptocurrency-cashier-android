package com.achpay.wallet.mvp.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseActivity;
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.OrderStatusMessage;
import com.achpay.wallet.model.event.OrderReadEvent;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.receipt.CoinReceiptActivity;
import com.achpay.wallet.mvp.setting.SettingExpenseActivity;
import com.achpay.wallet.mvp.webview.WebviewActivity;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.NumberUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OrderDetailActivity extends BaseActivity<OrderDtailPresenter> implements OrderDetailView, View.OnClickListener {
    private LinearLayout mOrderDetailLayout;

    private TextView mOrderCoinName;

    private TextView mOrderCoinAmount;
    private TextView mOrderCoinUnit;
    private TextView mReceivedCoinAmount;
    private TextView mReceivedCoinUnit;

    private TextView mOrderCurrencyAmount;
    private TextView mOrderCurrencyUnit;
    private TextView mReceivedCurrencyAmount;
    private TextView mReceivedCurrencyUnit;

    private LinearLayout mExceptionLayout;
    private TextView mExceptionStatus;
    private TextView mExceptionCoinAmount;
    private TextView mExceptionCoinUnit;
    private TextView mExceptionCurrencyAmount;
    private TextView mExceptionCurrencyUnit;

    private TextView mOrderId;
    private TextView mOrderStatus;
    private TextView mOrderTime;

    private TextView mOrderConfirmrations;
    private TextView mOrderConfirmrationsDes;

    private TextView mCheckOnBlockchain;

    private String orderId;
    private KProgressHUD progressHUD;
    private String orderAddress;
    private RelativeLayout mRightLayout;
    private TextView mRightText;
    private String coinId;

    private OrderResponse.OrderDetail mOrderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_new);
        EventBusUtil.register(this);

        mOrderDetailLayout = findViewById(R.id.order_detail_layout);

        mOrderCoinName = findViewById(R.id.order_detail_coin_name);

        mOrderCoinAmount = findViewById(R.id.order_detail_order_coin_amount);
        mOrderCoinUnit = findViewById(R.id.order_detail_order_coin_unit);
        mReceivedCoinAmount = findViewById(R.id.order_detail_received_coin_amount);
        mReceivedCoinUnit = findViewById(R.id.order_detail_received_coin_unit);

        mOrderCurrencyAmount = findViewById(R.id.order_detail_order_currency_amount);
        mOrderCurrencyUnit = findViewById(R.id.order_detail_order_currency_unit);
        mReceivedCurrencyAmount = findViewById(R.id.order_detail_received_currency_amount);
        mReceivedCurrencyUnit = findViewById(R.id.order_detail_received_currency_unit);


        mExceptionLayout = findViewById(R.id.order_detail_exception_layout);
        mExceptionStatus = findViewById(R.id.order_detail_exception_status);
        mExceptionCoinAmount = findViewById(R.id.order_detail_exception_coin_amount);
        mExceptionCoinUnit = findViewById(R.id.order_detail_exception_coin_unit);
        mExceptionCurrencyAmount = findViewById(R.id.order_detail_exception_currency_amount);
        mExceptionCurrencyUnit = findViewById(R.id.order_detail_exception_currency_unit);

        mOrderId = findViewById(R.id.order_detail_order_id);
        mOrderStatus = findViewById(R.id.order_detail_order_status);
        mOrderTime = findViewById(R.id.order_detail_order_time);

        mOrderConfirmrations = findViewById(R.id.order_detail_order_confirm_rations);
        mOrderConfirmrationsDes = findViewById(R.id.order_detail_order_confirm_rations_des);

        mCheckOnBlockchain = findViewById(R.id.order_detail_check_blockchain);

        mRightLayout = findViewById(R.id.titlebar_right_layout);
        mRightText = findViewById(R.id.titlebar_right_text);
        mRightText.setText(R.string.order_detail_continue);
        mRightText.setTextColor(getResources().getColor(R.color.input_confirm));

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.order_detail_title));

        mTitleBack.setOnClickListener(this);
        mCheckOnBlockchain.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);

        orderId = getIntent().getStringExtra(User.ORDER_ID);

        if (orderId != null) {
            MvpPre.getOrderHistory(orderId);
        }
    }

    @Override
    protected OrderDtailPresenter bindPresenter() {
        return new OrderDtailPresenter(this);
    }

    @Override
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
                            OrderDetailActivity.this.finish();
                        }
                    });
        }
        if (!progressHUD.isShowing()) {
            progressHUD.show();
        }
    }

    @Override
    public void dismissProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.order_detail_check_blockchain) {
            if (!TextUtils.isEmpty(orderAddress)) {
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra(TransParams.WEB_ADDRESS, orderAddress);
                intent.putExtra(TransParams.COIN_ID, coinId);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        } else if (v.getId() == R.id.titlebar_right_text) {
            // TODO: 2018/6/26 继续支付

/*
            Intent intent = new Intent();
            intent.setClass(this, CoinReceiptActivity.class);

            intent.putExtra(TransParams.TYPE, TransParams.CURRENCY);//类型为法币
            intent.putExtra(TransParams.CURRENCY_NAME, currencyName);//法币名字
            intent.putExtra(TransParams.COIN_NAME, coinName);//需要转换的虚拟币名字
            intent.putExtra(TransParams.AMOUNT, amount.getText().toString().trim());//法币金额
            intent.putExtra(TransParams.ORDER_ID, orderId);//订单号
            startActivity(intent);
            getSelfActivity().finish();
*/
        }
    }

    @Override
    public void setOrderDetail(OrderResponse.OrderDetail mOrderDetail) {
        this.mOrderDetail = mOrderDetail;

        coinId = String.valueOf(mOrderDetail.getCryptoCurrencyId());

        Cryptocurrency mCryptocurrency = CryptocurrencyManger.getInstance()
                .queryCryptocurrencyById(coinId);

//        String coinName = mCryptocurrency.getCryptoCurrency();
        String coinName = mOrderDetail.getCryptoCurrency();
        String currencyName = mOrderDetail.getCurrency();

        mOrderCoinName.setText(coinName);
        mReceivedCoinUnit.setText(coinName);

        mOrderCoinAmount.setText(mOrderDetail.getQuantity());
        mOrderCoinUnit.setText(coinName);
        mOrderCurrencyAmount.setText(mOrderDetail.getQuantityFait());
        mOrderCurrencyUnit.setText(currencyName);


        mOrderId.setText(mOrderDetail.getOrderId());
        mOrderStatus.setText(CommonUtil.transOrderStatus(this, mOrderDetail.getResult()));
        mOrderTime.setText(mOrderDetail.getReceiveTime());

        mOrderConfirmrations.setText(String.valueOf(mOrderDetail.getConfirmationNums()));

        orderAddress = mOrderDetail.getAddress();

        //只有成功才显示实收金额
        if (mOrderDetail.getResult().equals(ResponseParam.SUCCESS)
                || mOrderDetail.getResult().equals(ResponseParam.MSUCCESS)
                || mOrderDetail.getResult().equals(ResponseParam.LSUCCESS)) {

            mReceivedCoinAmount.setText(mOrderDetail.getReceiveQuantity());
            mReceivedCurrencyAmount.setText(mOrderDetail.getReceiveQuantityFait());
            mReceivedCurrencyUnit.setText(currencyName);
        }

        //只有成功或者确认中的才判断是否多收或少收
        if (mOrderDetail.getResult().equals(ResponseParam.SUCCESS)
                || mOrderDetail.getResult().equals(ResponseParam.MSUCCESS)
                || mOrderDetail.getResult().equals(ResponseParam.CONFIRMING)
                || mOrderDetail.getResult().equals(ResponseParam.LSUCCESS)) {

            double orderCoinAmount = Double.parseDouble(mOrderDetail.getQuantity());
            double orderCurrencyAmount = Double.parseDouble(mOrderDetail.getQuantityFait());
            double receiveCoinAmount = Double.parseDouble(mOrderDetail.getReceiveQuantity());
            double receiveCurrencyAmount = Double.parseDouble(mOrderDetail.getReceiveQuantityFait());
//            double receiveCurrencyAmount = (orderCurrencyAmount * receiveCoinAmount) / orderCoinAmount;

            if (orderCoinAmount > receiveCoinAmount) {
                mExceptionLayout.setVisibility(View.VISIBLE);
                mExceptionStatus.setText(getString(R.string.order_detail_less));
                mExceptionCoinAmount.setText(NumberUtils.formatDouble8(orderCoinAmount - receiveCoinAmount));
                mExceptionCoinUnit.setText(coinName);
                mExceptionCurrencyAmount.setText(NumberUtils.formatDouble2(orderCurrencyAmount - receiveCurrencyAmount));
                mExceptionCurrencyUnit.setText(currencyName);

                //继续支付
                //mRightLayout.setVisibility(View.VISIBLE);
            } else if (orderCoinAmount < receiveCoinAmount) {
                mExceptionLayout.setVisibility(View.VISIBLE);
                mExceptionStatus.setText(getString(R.string.order_detail_more));
                mExceptionCoinAmount.setText(NumberUtils.formatDouble8(receiveCoinAmount - orderCoinAmount));
                mExceptionCoinUnit.setText(coinName);
                mExceptionCurrencyAmount.setText(NumberUtils.formatDouble2(receiveCurrencyAmount - orderCurrencyAmount));
                mExceptionCurrencyUnit.setText(currencyName);
            }
        }

        //只有链上币种才显示区块确认数
        if (mCryptocurrency.getCoinType().equals(TransParams.BLOCKCHAIN)) {
            mOrderConfirmrations.setVisibility(View.VISIBLE);
            mOrderConfirmrationsDes.setVisibility(View.VISIBLE);
        }

        // TODO: 2018/6/13 判断其他币种
//        if (coinId.equals("1")) {//BTC
//            mCheckOnBlockchain.setVisibility(View.VISIBLE);
//        } else if (coinId.equals("2")) {//ETH
//            String newWebName = mCheckOnBlockchain.getText().toString().replace("blockchain", "etherscan");
//            mCheckOnBlockchain.setText(newWebName);
//            mCheckOnBlockchain.setVisibility(View.VISIBLE);
//        }

        mOrderDetailLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    /**
     * 推送收到的消息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatusMessageReceived(OrderStatusMessage message) {
        //收到最新的一笔订单推送
        if (this.orderId != null && this.orderId.equals(message.getOrderId())) {

            mOrderStatus.setText(CommonUtil.transOrderStatus(this, message.getResult()));
            mOrderConfirmrations.setText(String.valueOf(message.getConfirmBlockNums()));

            //只有成功才显示实收金额
            if (message.getResult().equals(ResponseParam.SUCCESS)
                    || message.getResult().equals(ResponseParam.MSUCCESS)
                    || message.getResult().equals(ResponseParam.LSUCCESS)) {

                if (mOrderDetail != null) {
                    mReceivedCoinAmount.setText(mOrderDetail.getReceiveQuantity());
                    mReceivedCurrencyAmount.setText(mOrderDetail.getReceiveQuantityFait());
                    mReceivedCurrencyUnit.setText(mOrderDetail.getCurrency());
                }
            }
        }
    }
}
