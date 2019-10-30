package com.achpay.wallet.model;

import com.achpay.wallet.utils.GsonUtil;

public class AddressOrderInfo {
    String merchatId;
    String orderId;

    public AddressOrderInfo() {
    }

    public AddressOrderInfo(String merchatId, String orderId) {
        this.merchatId = merchatId;
        this.orderId = orderId;
    }

    public String getMerchatId() {
        return merchatId;
    }

    public void setMerchatId(String merchatId) {
        this.merchatId = merchatId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return GsonUtil.objectToJson(this);
    }
}
