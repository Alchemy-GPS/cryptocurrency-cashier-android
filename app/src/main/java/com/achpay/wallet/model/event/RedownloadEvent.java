package com.achpay.wallet.model.event;

public class RedownloadEvent {
    private boolean redownload;

    public RedownloadEvent(boolean redownload) {
        this.redownload = redownload;
    }

    public boolean isRedownload() {
        return redownload;
    }

    public void setRedownload(boolean redownload) {
        this.redownload = redownload;
    }
}
