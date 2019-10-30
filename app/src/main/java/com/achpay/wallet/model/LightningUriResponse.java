package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LightningUriResponse implements Serializable {

    /**
     * returnCode : null
     * returnMsg : null
     * index : null
     * invoice : null
     * isSettled : null
     * value : null
     * uris : 0278c5e0c18d6302872166770c91cf5a6cb2b96e02386f3bd5f61e6de51b741e19@13.250.21.97:9735
     */

    @SerializedName("returnCode")
    private Object returnCode;
    @SerializedName("returnMsg")
    private Object returnMsg;
    @SerializedName("index")
    private Object index;
    @SerializedName("invoice")
    private Object invoice;
    @SerializedName("isSettled")
    private Object isSettled;
    @SerializedName("value")
    private Object value;
    @SerializedName("uris")
    private String uris;

    public Object getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Object returnCode) {
        this.returnCode = returnCode;
    }

    public Object getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(Object returnMsg) {
        this.returnMsg = returnMsg;
    }

    public Object getIndex() {
        return index;
    }

    public void setIndex(Object index) {
        this.index = index;
    }

    public Object getInvoice() {
        return invoice;
    }

    public void setInvoice(Object invoice) {
        this.invoice = invoice;
    }

    public Object getIsSettled() {
        return isSettled;
    }

    public void setIsSettled(Object isSettled) {
        this.isSettled = isSettled;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getUris() {
        return uris;
    }

    public void setUris(String uris) {
        this.uris = uris;
    }
}
