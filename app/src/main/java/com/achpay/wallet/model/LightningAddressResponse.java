package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LightningAddressResponse implements Serializable {

    /**
     * returnCode : null
     * returnMsg : null
     * index : 129
     * invoice : lntb1m1pd57pl0pp5uhpekfzakvkv4n0hvrt0umyufznrtq7vtvjj3x5zq6yrmqlhtpusdqld35kzmn8vd582ctwyqerqvfcxqmnzdgcqzysxqyz5vqjkuk89r4lrut7wrn8mrrxlrfswy3ffkc5ykzwey07mj9xldw9qgjw8sumgt5k2hhlsgseqmctcy35538xdutwumk9qwufkj3z6uf4tgqvsrtsx
     * isSettled : null
     * value : null
     * uris : null
     */

    @SerializedName("returnCode")
    private Object returnCode;
    @SerializedName("returnMsg")
    private Object returnMsg;
    @SerializedName("index")
    private int index;
    @SerializedName("invoice")
    private String invoice;
    @SerializedName("isSettled")
    private Object isSettled;
    @SerializedName("value")
    private Object value;
    @SerializedName("uris")
    private Object uris;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
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

    public Object getUris() {
        return uris;
    }

    public void setUris(Object uris) {
        this.uris = uris;
    }
}
