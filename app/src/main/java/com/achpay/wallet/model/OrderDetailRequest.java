package com.achpay.wallet.model;

import com.achpay.wallet.utils.CommonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetailRequest implements Serializable {


    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * sysId : APP
     * orderId : 2018072510330100001
     * page : 1
     * pageNum : 1
     * queryType : sysSerialNum
     * sign : 23232323232
     */

    @SerializedName("merchantId")
    private String merchantId = CommonUtil.getMerchantId();
    @SerializedName("sysId")
    private String sysId = CommonUtil.getSysId();
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("page")
    private int page = 1;
    @SerializedName("pageNum")
    private int pageNum = 1;
    @SerializedName("queryType")
    private String queryType = "inSerialNum";
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
