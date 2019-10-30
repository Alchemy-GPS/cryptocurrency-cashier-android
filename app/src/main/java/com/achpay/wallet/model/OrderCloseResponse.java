package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderCloseResponse implements Serializable {

    /**
     * sysflowno : gw2018080703285200001
     * merchantId : 64c6b0e70ca84463b873f4349e3c7140
     * sysId : APP
     */

    @SerializedName("sysflowno")
    private String sysflowno;
    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("sysId")
    private String sysId;

    public String getSysflowno() {
        return sysflowno;
    }

    public void setSysflowno(String sysflowno) {
        this.sysflowno = sysflowno;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
}
