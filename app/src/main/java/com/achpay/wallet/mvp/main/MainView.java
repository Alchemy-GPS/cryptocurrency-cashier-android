package com.achpay.wallet.mvp.main;

import android.support.v4.app.FragmentActivity;

import com.achpay.wallet.base.view.IView;
import com.achpay.wallet.model.UpdateAppResponse;

public interface MainView extends IView<FragmentActivity> {
    void showPoint(boolean show);

    void showUpdate(UpdateAppResponse response);

    void showDownloadProgress();

    void initViewPager();

    void showLoading();

    void dismiss();

    void loginSuccess();

    void loginFail();
}