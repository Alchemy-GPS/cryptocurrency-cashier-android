package com.achpay.wallet.model.event;

/**
 * Created by dawnton on 2017/5/3.
 */

public class OrderReadEvent {
    private boolean isRead;
    private String orderId;

    public OrderReadEvent(boolean isRead, String orderId) {
        this.isRead = isRead;
        this.orderId = orderId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
