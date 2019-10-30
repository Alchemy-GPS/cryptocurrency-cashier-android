package com.achpay.wallet.mvp.transaction.statements;

import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.model.StatementRequest;
import com.achpay.wallet.model.StatementResponse;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;

import java.util.List;

public class StatementPresenter extends BasePresenter<StatementView> {
    public StatementPresenter(StatementView view) {
        super(view);
    }

    public void getStatementHistory() {

        StatementRequest request = new StatementRequest(CommonUtil.getLastYearDate(), CommonUtil.getTodayDate(), 1);

        request.setSign("ACHPAY");

        RetrofitUtil.getInstance().getStatementHistory(request, new BaseSubscriber<UniteResponse<StatementResponse>>() {

            @Override
            public void onResponse(UniteResponse<StatementResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (MvpRef != null) {
                    if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                        StatementResponse StatementResponse = response.getData();
                        List<StatementResponse.Detail> mDetails = StatementResponse.getDetails();
                        if (mDetails != null && mDetails.size() != 0) {
                            MvpRef.get().setStatementHistory(mDetails);
                        }
                    }
                    MvpRef.get().stopRefresh();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (MvpRef != null) {
                    MvpRef.get().stopRefresh();
                }
            }
        });
    }

    public void getMoreIntervalHistory(int page) {

        StatementRequest request = new StatementRequest(CommonUtil.getLastYearDate(), CommonUtil.getTodayDate(), page);

        request.setSign("ACHPAY");

        RetrofitUtil.getInstance().getStatementHistory(request, new BaseSubscriber<UniteResponse<StatementResponse>>() {
            @Override
            public void onResponse(UniteResponse<StatementResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (MvpRef != null) {
                    if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                        StatementResponse statementResponse = response.getData();
                        List<StatementResponse.Detail> mDetails = statementResponse.getDetails();
                        if (mDetails != null && mDetails.size() != 0) {
                            MvpRef.get().addStatementHistory(mDetails);
                        } else {
                            MvpRef.get().stopLoading(false);
                        }
                    } else {
                        MvpRef.get().stopLoading(true);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (MvpRef != null) {
                    MvpRef.get().stopLoading(true);
                }
            }
        });
    }
}
