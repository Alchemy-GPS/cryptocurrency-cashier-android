package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RaidenQRParams implements Serializable {

    @SerializedName("token")
    private String token;
    @SerializedName("proxy")
    private String proxy;
    @SerializedName("amount")
    private String amount;
    @SerializedName("sysFlowNo")
    private String sysFlowNo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSysFlowNo() {
        return sysFlowNo;
    }

    public void setSysFlowNo(String sysFlowNo) {
        this.sysFlowNo = sysFlowNo;
    }
}
