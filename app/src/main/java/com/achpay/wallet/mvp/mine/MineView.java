package com.achpay.wallet.mvp.mine;

import android.support.v4.app.FragmentActivity;

import com.achpay.wallet.base.view.IView;
import com.achpay.wallet.model.UserInfoResponse;

import java.util.List;

public interface MineView extends IView<FragmentActivity> {
    void setUserInfo(UserInfoResponse userInfoResponse);
    void setDatas(List<UserInfoResponse.AccountInfos> accountInfos);
    void stopRefresh();
}
