package com.achpay.wallet.mvp.history;

import android.content.Context;

import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.model.OrderHistoryTypeRequest;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HistoryPresenter extends BasePresenter<HistoryView> {

    public HistoryPresenter(HistoryView view) {
        super(view);
    }

    public void getOrderHistory(Context mContext, int coinId) {

        OrderHistoryTypeRequest request = new OrderHistoryTypeRequest();

        request.setPage(1);
        request.setSign("ACHPAY");
        request.setCryptoCurrencyId(coinId);

        RetrofitUtil.getInstance().getOrderHistoryOfType(request, new BaseSubscriber<UniteResponse<OrderResponse>>() {
            @Override
            public void onResponse(UniteResponse<OrderResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (MvpRef != null) {
                    if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {

                        OrderResponse orderResponse = response.getData();

                        if (orderResponse != null) {

                            List<OrderResponse.OrderDetail> mDetails = orderResponse.getOrderDetails();

                            if (mDetails != null && mDetails.size() != 0) {
                                MvpRef.get().setOrderHistory(mDetails);
                            }
                        }
                    }
                    MvpRef.get().stopRefresh();
                    MvpRef.get().dismissProgress();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("onError == " + "获取历史信息失败");
                if (MvpRef != null) {
                    MvpRef.get().dismissProgress();
                }
            }
        });
    }

    public void getMoreHistory(Context mContext, int coinId, int page) {

        OrderHistoryTypeRequest request = new OrderHistoryTypeRequest();

        request.setPage(page);
        request.setSign("ACHPAY");
        request.setCryptoCurrencyId(coinId);

        RetrofitUtil.getInstance().getOrderHistoryOfType(request, new BaseSubscriber<UniteResponse<OrderResponse>>() {
            @Override
            public void onResponse(UniteResponse<OrderResponse> response) {

                Log.i(GsonUtil.objectToJson(response));

                if (MvpRef != null) {
                    if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {

                        OrderResponse orderResponse = response.getData();

                        if (orderResponse != null) {

                            List<OrderResponse.OrderDetail> mDetails = orderResponse.getOrderDetails();

                            if (mDetails != null && mDetails.size() != 0) {
                                MvpRef.get().addOrderHistory(mDetails);
                            } else {
                                MvpRef.get().stopLoading(false);//没有更多了
                            }
                        } else {
                            MvpRef.get().stopLoading(true);//可能还有更多记录
                        }
                    } else {
                        MvpRef.get().stopLoading(true);//可能还有更多记录
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("onError == " + "获取历史信息失败");
                if (MvpRef != null) {
                    MvpRef.get().stopLoading(true);
                }
            }
        });
    }
}
