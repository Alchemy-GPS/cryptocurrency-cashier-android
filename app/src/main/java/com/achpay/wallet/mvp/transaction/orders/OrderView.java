package com.achpay.wallet.mvp.transaction.orders;

import android.support.v4.app.FragmentActivity;

import com.achpay.wallet.base.view.IView;
import com.achpay.wallet.model.OrderResponse;

import java.util.List;

public interface OrderView extends IView<FragmentActivity> {
    void setTransactionHistory(List<OrderResponse.OrderDetail> mDetails);

    void addTransactionHistory(List<OrderResponse.OrderDetail> mDetails);

    void stopLoading(boolean haveMore);

    void stopRefresh();
}
