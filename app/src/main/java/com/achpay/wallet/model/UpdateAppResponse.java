package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateAppResponse implements Serializable {

    @SerializedName("returnCode")
    public String returnCode;
    @SerializedName("returnMsg")
    public String returnMsg;
    /**
     * 版本号
     */
    @SerializedName("versinCode")
    private String versinCode;
    /**
     * app下载地址
     */
    @SerializedName("url")
    private String url;
    /**
     * 是否新版本
     */
    @SerializedName("newVersion")
    private String newVersion;
    /**
     * apk md5
     */
    @SerializedName("apkMd5")
    private String apkMd5;
    /**
     * 是否强制更新
     */
    @SerializedName("forceUpdate")
    private String forceUpdate;
    /**
     * 更新内容
     */
    @SerializedName("updateInfo")
    private String updateInfo;

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

    public String getVersinCode() {
        return versinCode;
    }

    public void setVersinCode(String versinCode) {
        this.versinCode = versinCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}
