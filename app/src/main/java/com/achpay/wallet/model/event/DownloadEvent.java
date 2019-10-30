package com.achpay.wallet.model.event;

/**
 * Created by dawnton on 2017/5/3.
 */

public class DownloadEvent {
    private int progress;
    private int status;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
