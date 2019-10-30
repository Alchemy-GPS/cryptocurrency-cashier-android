package com.achpay.wallet.mvp.order;

import android.app.Activity;

import com.achpay.wallet.base.view.IView;
import com.achpay.wallet.model.OrderResponse;

public interface OrderDetailView extends IView<Activity> {

    void setOrderDetail(OrderResponse.OrderDetail orderDetail);

    void showProgress();

    void dismissProgress();
}
