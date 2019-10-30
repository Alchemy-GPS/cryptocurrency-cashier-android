package com.achpay.wallet.mvp.receipt;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.achpay.wallet.R;
import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.model.OrderCreateRequest;
import com.achpay.wallet.model.OrderCreateResponse;
import com.achpay.wallet.model.RaidenPayResponse;
import com.achpay.wallet.model.RaidenQRParams;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
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
import com.achpay.wallet.utils.NumberUtils;
import com.achpay.wallet.utils.RaidenParamParser;
import com.achpay.wallet.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class CoinReceiptPresenter extends BasePresenter<CoinReceiptView> {

    //一个虚拟币转换为法币的汇率(统一用此汇率计算)
    private double rate;
    private String salesType;
    private String salesKind;
    private String salesValue;
    private String raidenPayUrl;
    private Context mContext;

    public CoinReceiptPresenter(CoinReceiptView view) {
        super(view);
    }

    public void createOrder(final Context mContext, String currencyId, final String coinId, String currencyAmount, String orderId, boolean isSegwit) {
        this.mContext = mContext;
        OrderCreateRequest request = new OrderCreateRequest();
        request.setOrderId(orderId);
        request.setOrderQuantityFait(currencyAmount);
        request.setCurrencyId(currencyId);
        request.setCryptoCurrencyId(coinId);
        request.setTimezone(CommonUtil.getTimeZone());

        request.setTerminalId(CommonUtil.getDeviceId());
        request.setSign("ACHPAY");

        if (CommonUtil.supportSegwit(coinId) && isSegwit) {
            request.setAddressType(TransParams.ADDRESS_SEGWIT);
        } else {
            request.setAddressType(TransParams.ADDRESS_NORMAL);
        }

        request.setIsSales("Y");
        List<OrderCreateRequest.Sales> salesList = new ArrayList<>();
        OrderCreateRequest.Sales sale = new OrderCreateRequest.Sales();
        sale.setSalesKind(salesKind);
        sale.setSalesType(salesType);
        sale.setSalesValue(salesValue);
        salesList.add(sale);
        request.setSalesList(salesList);

        RetrofitUtil.getInstance().createOrder(request, new BaseSubscriber<UniteResponse<OrderCreateResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {
                MvpRef.get().showProgress();
            }

            @Override
            public void onResponse(UniteResponse<OrderCreateResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    OrderCreateResponse orderCreateResponse = response.getData();

                    if (MvpRef != null) {

                        MvpRef.get().setFee(orderCreateResponse.getFastestFee(), orderCreateResponse.getUnit(), orderCreateResponse.getFastestFeeFait(), "");

                        MvpRef.get().setCoinAmount(orderCreateResponse.getQuantity());

                        MvpRef.get().setCurrencyAmount(orderCreateResponse.getQuantityFait());

                        double discountOrExtraAmount = Double.parseDouble(orderCreateResponse.getQuantityFait()) - Double.parseDouble(orderCreateResponse.getOrderQuantityFait());

                        MvpRef.get().setDiscountOrExtraAmount(NumberUtils.formatDouble2(Math.abs(discountOrExtraAmount)));

                        if (CommonUtil.supportRaidenNetwork(coinId)) {

                            raidenPayUrl = orderCreateResponse.getAddress();

                            RaidenParamParser.Parameter parameter = RaidenParamParser.parseString(orderCreateResponse.getAddress());
                            RaidenQRParams params = new RaidenQRParams();
                            if (parameter != null) {
                                params.setAmount(parameter.getAmount());
                                params.setProxy(parameter.getProxy());
                                params.setToken(parameter.getToken());
                                params.setSysFlowNo(parameter.getSysFlowNo());

                                String paramJson = GsonUtil.objectToJson(params);

                                MvpRef.get().setAddress(parameter.getToken());

                                MvpRef.get().showQRCode(paramJson);
                            }
                        } else {
                            MvpRef.get().setAddress(orderCreateResponse.getAddress());
                        }

                        MvpRef.get().dismissProgress();
                    }

                    //创建订单成功以后,主动关于服务器做连接
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
                }else if(response.getReturnCode().equals(ResponseParam.AMOUNT_EXCEED_LIMIT)){

                    Toast.makeText(mContext, R.string.receipt_amount_exceed_limit, Toast.LENGTH_SHORT).show();

                    if (MvpRef != null) {
                        MvpRef.get().dismissProgress();
                        MvpRef.get().showQRCode("");
                    }
                } else {
                    if (MvpRef != null) {
                        MvpRef.get().dismissProgress();
                        MvpRef.get().showQRCode("");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("onError   创建订单失败");
                if (MvpRef != null) {
                    MvpRef.get().dismissProgress();
                    MvpRef.get().showQRCode("");
                }
            }
        });
    }

    public void raidenPay(String userAddress) {
        if (MvpRef != null) {
            MvpRef.get().showProgress();
        }

        RetrofitUtil.getInstance().raidenPay(raidenPayUrl.concat(userAddress), new BaseSubscriber<RaidenPayResponse>() {
            @Override
            public void onResponse(RaidenPayResponse raidenPayResponse) {
                Log.i(GsonUtil.objectToJson(raidenPayResponse));

                if (MvpRef != null) {
                    MvpRef.get().dismissProgress();
                }

                if (raidenPayResponse.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    //扣款成功
                    Log.i("扣款成功");
                } else {
                    Toast.makeText(mContext, raidenPayResponse.getReturnMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (MvpRef != null) {
                    MvpRef.get().dismissProgress();
                }
                Toast.makeText(mContext, mContext.getString(R.string.deduct_currency_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeExtraAmount(String extraType, String extraAmount) {
        if (User.EXTRA_CASH.equals(extraType)) {
            salesType = User.COUPON;

            salesKind = User.ADD;

            salesValue = extraAmount;

        } else if (User.EXTRA_PERCENT.equals(extraType)) {

            salesType = User.DISCOUNT;

            salesKind = User.ADD;

            salesValue = extraAmount;
        }
    }

    public void changeDiscountAmount(String discountType, String discountAmount) {
        if (User.DISCOUNT_CASH.equals(discountType)) {
            salesType = User.COUPON;

            salesKind = User.SUBTRACT;

            salesValue = discountAmount;
        } else if (User.DISCOUNT_PERCENT.equals(discountType)) {
            salesType = User.DISCOUNT;

            salesKind = User.SUBTRACT;

            salesValue = discountAmount;
        }
    }

    public void calculateAmount(Context mContext) {
        String settingAmountType = SharedPreferenceUtil.getPref(mContext).getString(User.SETTING_AMOUNT_TYPE, User.SETTING_AMOUNT_DISCOUNT);

        if (User.SETTING_AMOUNT_DISCOUNT.equals(settingAmountType)) {
            salesKind = User.SUBTRACT;

            String discountType = SharedPreferenceUtil.getPref(mContext).getString(User.DISCOUNT_TYPE, User.DISCOUNT_CASH);

            if (User.DISCOUNT_CASH.equals(discountType)) {
                salesType = User.COUPON;

                salesValue = SharedPreferenceUtil.getPref(mContext).getString(User.DISCOUNT_CASH_AMOUNT, "0");
            } else if (User.DISCOUNT_PERCENT.equals(discountType)) {
                salesType = User.DISCOUNT;

                salesValue = SharedPreferenceUtil.getPref(mContext).getString(User.DISCOUNT_PERCENT_AMOUNT, "0");
            }
        } else if (User.SETTING_AMOUNT_EXTRA.equals(settingAmountType)) {
            salesKind = User.ADD;

            String extraType = SharedPreferenceUtil.getPref(mContext).getString(User.EXTRA_TYPE, User.EXTRA_CASH);
            if (User.EXTRA_CASH.equals(extraType)) {
                salesType = User.COUPON;

                salesValue = SharedPreferenceUtil.getPref(mContext).getString(User.EXTRA_CASH_AMOUNT, "0");
            } else if (User.EXTRA_PERCENT.equals(extraType)) {
                salesType = User.DISCOUNT;

                salesValue = SharedPreferenceUtil.getPref(mContext).getString(User.EXTRA_PERCENT_AMOUNT, "0");
            }
        }
    }
}
