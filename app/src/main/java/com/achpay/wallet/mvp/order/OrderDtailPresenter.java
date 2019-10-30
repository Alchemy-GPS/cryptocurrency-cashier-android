package com.achpay.wallet.mvp.order;

import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.model.OrderDetailRequest;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class OrderDtailPresenter extends BasePresenter<OrderDetailView> {

    public OrderDtailPresenter(OrderDetailView view) {
        super(view);
    }

    public void getOrderHistory(String orderId) {
        OrderDetailRequest request = new OrderDetailRequest();
        request.setSign("ACHPAY");
        request.setOrderId(orderId);

        RetrofitUtil.getInstance().getOrderDetail(request, new BaseSubscriber<UniteResponse<OrderResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (MvpRef != null) {
                    MvpRef.get().showProgress();
                }
            }

            @Override
            public void onResponse(UniteResponse<OrderResponse> response) {

                Log.i(GsonUtil.objectToJson(response));

                if (MvpRef != null) {
                    if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                        OrderResponse orderResponse = response.getData();
                        List<OrderResponse.OrderDetail> mOrderDetails = orderResponse.getOrderDetails();
                        if (mOrderDetails != null && mOrderDetails.size() != 0) {
                            MvpRef.get().setOrderDetail(mOrderDetails.get(0));
                        }
                    }
                    MvpRef.get().dismissProgress();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (MvpRef != null) {
                    MvpRef.get().dismissProgress();
                }
            }
        });
    }
}
