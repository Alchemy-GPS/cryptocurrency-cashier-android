package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRequest implements Serializable{

    public LoginRequest() {
    }

    public LoginRequest(String loginId, String merchantName, String sessionid, String sysId) {
        this.loginId = loginId;
        this.merchantName = merchantName;
        this.sessionid = sessionid;
        this.sysId = sysId;
    }

    /**
     * loginId : 1234
     * loginPassword : NmFkN2Q1MWY2ZDhhZTgxODBjYzY2MGMxYTEyNTE2ZmI
     * sysId : APP
     */

    @SerializedName("loginId")
    private String loginId;
    @SerializedName("loginPassword")
    private String loginPassword;
    @SerializedName("merchantName")
    private String merchantName;
    @SerializedName("sessionid")
    private String sessionid;
    @SerializedName("sysId")
    private String sysId;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
}
