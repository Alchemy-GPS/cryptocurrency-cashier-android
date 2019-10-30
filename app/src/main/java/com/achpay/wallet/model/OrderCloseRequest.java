package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderCloseRequest implements Serializable {

    /**
     * merchantId : 64c6b0e70ca84463b873f4349e3c7140
     * sysId : APP
     * sysflowno : gw2018080308075300001
     * sign :
     */

    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("sysId")
    private String sysId;
    @SerializedName("sysflowno")
    private String sysflowno;
    @SerializedName("sign")
    private String sign;

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

    public String getSysflowno() {
        return sysflowno;
    }

    public void setSysflowno(String sysflowno) {
        this.sysflowno = sysflowno;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
