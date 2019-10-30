package com.achpay.wallet.mvp.history;

import android.app.Activity;

import com.achpay.wallet.base.view.IView;
import com.achpay.wallet.model.OrderResponse;

import java.util.List;

public interface HistoryView extends IView<Activity> {

    void setOrderHistory(List<OrderResponse.OrderDetail> mDetails);

    void addOrderHistory(List<OrderResponse.OrderDetail> mDetails);

    void stopLoading(boolean haveMore);

    void stopRefresh();

    void showProgress();

    void dismissProgress();
}
