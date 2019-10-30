package com.achpay.wallet.mvp.receipt;

import android.app.Activity;

import com.achpay.wallet.base.view.IView;

public interface CoinReceiptView extends IView<Activity> {
    void showQRCode(String address);

    void showQRCodeWithAmount(String address,String amount);

    void setCurrencyAmount(String amount);

    void setCoinAmount(String amount);

    void setFee(String fee, String unit, String currFee, String percentage);

    void setDiscountOrExtraAmount(String amount);

    void setAmount(String coinAmount,String currencyAmount);

    void setAddress(String address);

    void showProgress();

    void dismissProgress();
}
