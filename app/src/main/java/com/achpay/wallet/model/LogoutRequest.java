package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LogoutRequest implements Serializable {

    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * sysId : APP
     */

    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("sysId")
    private String sysId;

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
