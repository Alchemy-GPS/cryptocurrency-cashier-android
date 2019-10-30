package com.achpay.wallet.mvp.assets;

import android.app.Activity;

import com.achpay.wallet.base.view.BaseActivity;
import com.achpay.wallet.base.view.IView;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.UserInfoResponse;

import java.util.List;

public interface AssetsView extends IView<Activity> {
    void setDatas(List<UserInfoResponse.AccountInfos> merchantAccounts);

    void addOrderHistory(List<UserInfoResponse.AccountInfos> merchantAccounts);

    void stopLoading(boolean haveMore);

    void stopRefresh();

    void showProgress();

    void dismissProgress();
}
