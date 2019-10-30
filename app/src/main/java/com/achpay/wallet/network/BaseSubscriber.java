package com.achpay.wallet.network;

import com.achpay.wallet.model.event.SessionTimeoutEvent;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseSubscriber<T> implements Observer<T> {
    @Override
    public void onNext(T t) {
        if (t != null) {
            if (t instanceof UniteResponse) {
                UniteResponse response = (UniteResponse) t;
                if (ResponseParam.SESSION_TIMEOUT.equals(response.getReturnCode())) {
                    SessionTimeoutEvent timeoutEvent = new SessionTimeoutEvent(ResponseParam.SESSION_TIMEOUT);
                    EventBusUtil.post(timeoutEvent);
                }
            }
            this.onResponse(t);
        } else {
            onError(new NullPointerException("Response Content is NULL"));
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    public abstract void onResponse(T t);
}
