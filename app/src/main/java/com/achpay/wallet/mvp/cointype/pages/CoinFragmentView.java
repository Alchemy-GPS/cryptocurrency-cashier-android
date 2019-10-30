package com.achpay.wallet.mvp.cointype.pages;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.achpay.wallet.base.view.IView;

public interface CoinFragmentView extends IView<FragmentActivity> {
    void setDatas();

    void stopLoading(boolean haveMore);

    void stopRefresh();
}
