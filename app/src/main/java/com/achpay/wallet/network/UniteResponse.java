package com.achpay.wallet.network;

import com.google.gson.annotations.SerializedName;

public class UniteResponse<T> {
    @SerializedName("returnCode")
    public String returnCode;
    @SerializedName("returnMsg")
    public String returnMsg;
    @SerializedName("data")
    public T data;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
