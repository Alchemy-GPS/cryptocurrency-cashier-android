package com.achpay.wallet.mvp.transaction.orders;

import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.model.OrderHistoryIntervalRequest;
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

import io.reactivex.disposables.Disposable;

public class OrderPresenter extends BasePresenter<OrderView> {

    public OrderPresenter(OrderView view) {
        super(view);
    }

    public void getIntervalHistory(String interval) {

        OrderHistoryIntervalRequest request = new OrderHistoryIntervalRequest(1);

        /*if (interval.equals(TransParams.LAST_THREE_DAY)) {
            request.setFromDate(CommonUtil.getLastThreeDaysDate());
        } else if (interval.equals(TransParams.LAST_WEEK)) {
            request.setFromDate(CommonUtil.getLastWeekDate());
        } else {
            request.setFromDate(CommonUtil.getLastMonthDate());
        }*/

        request.setFromDate(CommonUtil.getLastYearDate());
        request.setToDate(CommonUtil.getTodayDate());
        request.setSign("ACHPAY");

        RetrofitUtil.getInstance().getOrderHistoryOfInterval(request, new BaseSubscriber<UniteResponse<OrderResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onResponse(UniteResponse<OrderResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    OrderResponse orderResponse = response.getData();
                    List<OrderResponse.OrderDetail> mDetails = orderResponse.getOrderDetails();
                    if (mDetails != null && mDetails.size() != 0) {
                        MvpRef.get().setTransactionHistory(mDetails);
                    }
                }
                MvpRef.get().stopRefresh();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                MvpRef.get().stopRefresh();
            }
        });
    }

    public void getMoreIntervalHistory(String interval, int page) {

        OrderHistoryIntervalRequest request = new OrderHistoryIntervalRequest(page);

        /*if (interval.equals(TransParams.LAST_THREE_DAY)) {
            request.setFromDate(CommonUtil.getLastThreeDaysDate());
        } else if (interval.equals(TransParams.LAST_WEEK)) {
            request.setFromDate(CommonUtil.getLastWeekDate());
        } else {
            request.setFromDate(CommonUtil.getLastMonthDate());
        }*/

        request.setFromDate(CommonUtil.getLastYearDate());
        request.setToDate(CommonUtil.getTodayDate());
        request.setSign("ACHPAY");

        RetrofitUtil.getInstance().getOrderHistoryOfInterval(request, new BaseSubscriber<UniteResponse<OrderResponse>>() {
            @Override
            public void onResponse(UniteResponse<OrderResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    OrderResponse orderResponse = response.getData();
                    List<OrderResponse.OrderDetail> mDetails = orderResponse.getOrderDetails();
                    if (mDetails != null && mDetails.size() != 0) {
                        MvpRef.get().addTransactionHistory(mDetails);
                    } else {
                        MvpRef.get().stopLoading(false);
                    }
                } else {
                    MvpRef.get().stopLoading(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                MvpRef.get().stopLoading(true);
            }
        });
    }
}
