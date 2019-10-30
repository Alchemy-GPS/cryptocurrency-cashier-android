package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterRequest implements Serializable{

    @SerializedName("loginId")
    private String loginId;
    @SerializedName("merchantName")
    private String merchantName;
    @SerializedName("registMail")
    private String registMail;
    @SerializedName("loginPassword")
    private String loginPassword ;
    @SerializedName("againLoginPassword")
    private String againLoginPassword;
    @SerializedName("sign")
    private String sign;
    @SerializedName("sysId")
    private String sysId;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getRegistMail() {
        return registMail;
    }

    public void setRegistMail(String registMail) {
        this.registMail = registMail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getAgainLoginPassword() {
        return againLoginPassword;
    }

    public void setAgainLoginPassword(String againLoginPassword) {
        this.againLoginPassword = againLoginPassword;
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
