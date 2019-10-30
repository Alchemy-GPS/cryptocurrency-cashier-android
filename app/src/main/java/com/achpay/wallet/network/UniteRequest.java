package com.achpay.wallet.network;

import com.achpay.wallet.Constants;
import com.google.gson.annotations.SerializedName;
import com.achpay.wallet.ACHApplication;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.CommonUtil;

public class UniteRequest {

    /**
     * os : ANDROID
     * osVersion : 1.0.2
     * appVersion : 1.0
     * appId : com.achpay.wallet
     * appChannel : GOOGLE_PLAY
     * token : NmFkN2Q1MWY2ZDhhZTgxODBjYzY2MGMxYTEyNTE2ZmI
     * data : {"merchantId": "9d421ee24e39405abfd085302b24413f", "sysId": "APP"}
     */

    public UniteRequest() {

    }

    public UniteRequest(Object data) {
        this.data = data;
    }

    @SerializedName("os")
    public String os = "ANDROID";
    @SerializedName("osVersion")
    public String osVersion = AppUtil.getOsVersion();
    @SerializedName("serverVersion")
    public String serverVersion = Constants.serverVersion;
    @SerializedName("appId")
    public String appId = AppUtil.getPackageName(ACHApplication.APPLICATION);
    @SerializedName("appChannel")
    public String appChannel = AppUtil.getAppChannel(ACHApplication.APPLICATION);
    @SerializedName("token")
    public String token;
    @SerializedName("data")
    public Object data;

    public String getOs() {
        return os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public String getToken() {
        return token;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setToken() {
        this.token = CommonUtil.getToken(ACHApplication.APPLICATION);
    }

    public void setToken(String token) {
        this.token = token;
    }
}
