package com.achpay.wallet.mvp.transaction;

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

public class TransactionPresenter extends BasePresenter<TransactionView> {
    public TransactionPresenter(TransactionView view) {
        super(view);
    }

}
