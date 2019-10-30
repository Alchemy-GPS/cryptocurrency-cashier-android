package com.achpay.wallet.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.achpay.wallet.utils.AppUtil;

import java.io.Serializable;

public class UpdateAppRequest implements Serializable {
    /**
     * app 版本号
     */
    @SerializedName("appVersion")
    private String appVersion;
    /**
     * 项目包名，用以区分是什么项目的app
     */
    @SerializedName("packgeName")
    private String packgeName;
    /**
     * 操作系统 ，ios or android
     */
    @SerializedName("operationSystem")
    private String operationSystem;
    /**
     *
     */
    @SerializedName("osVersion")
    private String osVersion;

    public UpdateAppRequest(String appVersion, String packgeName) {
        this.appVersion = appVersion;
        this.operationSystem = "ANDROID";
        this.osVersion = AppUtil.getOsVersion();
        this.packgeName = packgeName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPackgeName() {
        return packgeName;
    }

    public void setPackgeName(String packgeName) {
        this.packgeName = packgeName;
    }

    public String getOperationSystem() {
        return operationSystem;
    }

    public void setOperationSystem(String operationSystem) {
        this.operationSystem = operationSystem;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}
