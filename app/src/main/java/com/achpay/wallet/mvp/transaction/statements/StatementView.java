package com.achpay.wallet.mvp.transaction.statements;

import android.support.v4.app.FragmentActivity;

import com.achpay.wallet.base.view.IView;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.StatementResponse;

import java.util.List;

public interface StatementView extends IView<FragmentActivity> {
    void setStatementHistory(List<StatementResponse.Detail> mDetails);

    void addStatementHistory(List<StatementResponse.Detail> mDetails);

    void stopLoading(boolean haveMore);

    void stopRefresh();
}
