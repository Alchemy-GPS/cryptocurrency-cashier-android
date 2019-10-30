package com.achpay.wallet.mvp.main;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.achpay.wallet.base.presenter.IPresenter;
import com.achpay.wallet.base.view.IView;

public class MainConnect {

    /**
     * 在此接口下添加View中的方法
     */
    public interface IMainView extends IView<FragmentActivity> {
        void showPoint(boolean show);
    }

    /**
     * 在此接口下添加Presenter中的方法
     */
    public interface IMainPresenter extends IPresenter {
        void showPoint(boolean show);
    }

    /**
     * 在此接口下添加Model中的方法
     */
    public interface IMainLogic {
        boolean login(String username, String password);
    }
}
