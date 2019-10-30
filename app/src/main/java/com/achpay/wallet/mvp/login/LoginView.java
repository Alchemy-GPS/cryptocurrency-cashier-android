package com.achpay.wallet.mvp.login;

import android.app.Activity;

import com.achpay.wallet.base.view.IView;

public interface LoginView extends IView<Activity> {
    void showLoading();

    void dismiss();

    void loginSuccess();

    void loginFail();
}
