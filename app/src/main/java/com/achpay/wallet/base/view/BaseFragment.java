package com.achpay.wallet.base.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.achpay.wallet.base.presenter.IPresenter;


public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView<FragmentActivity> {
    public static final String BUNDLE_TITLE = "BUNDLE_TITLE";

    protected P MvpPre;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MvpPre = bindPresenter();
    }

    protected abstract P bindPresenter();

    @Override
    public FragmentActivity getSelfActivity() {
        return getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * 在生命周期结束时，将presenter与view之间的联系断开，防止出现内存泄露
         */
        if (MvpPre != null) {
            MvpPre.detachView();
        }
    }
}
