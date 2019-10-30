package com.achpay.wallet.mvp.cointype;

import android.support.v4.app.FragmentActivity;

import com.achpay.wallet.base.view.IView;
import com.achpay.wallet.model.UpdateAppResponse;

public interface CoinTypeView extends IView<FragmentActivity> {

    void setCurrency(String currencyId);

    void setAmount(String amount);

    void initViewPager();

    void showLoading();

    void dismiss();

    void loginSuccess();

    void loginFail();

    void showUpdate(UpdateAppResponse response);

    void showDownloadProgress();
}
