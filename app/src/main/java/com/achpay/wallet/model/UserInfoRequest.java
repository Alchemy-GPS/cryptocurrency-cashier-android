package com.achpay.wallet.model;

import com.achpay.wallet.utils.CommonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserInfoRequest implements Serializable{

    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * targetCryptoCurrencyId : 1
     * targetCurrencyId : 1
     * sign : 23232323232
     */

    @SerializedName("merchantId")
    private String merchantId = CommonUtil.getMerchantId();
    @SerializedName("targetCryptoCurrencyId")
    private String targetCryptoCurrencyId = CommonUtil.getCryptoCurrencyId();
    @SerializedName("targetCurrencyId")
    private String targetCurrencyId = CommonUtil.getCurrencyId();
    @SerializedName("sign")
    private String sign = "";
    @SerializedName("sysId")
    private String sysId = CommonUtil.getSysId();

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTargetCryptoCurrencyId() {
        return targetCryptoCurrencyId;
    }

    public void setTargetCryptoCurrencyId(String targetCryptoCurrencyId) {
        this.targetCryptoCurrencyId = targetCryptoCurrencyId;
    }

    public String getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(String targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
}
