package com.achpay.wallet.model.event;

import java.io.Serializable;

public class TransactionSelectedEvent implements Serializable {
    private boolean needRefresh;

    public TransactionSelectedEvent(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }
}
