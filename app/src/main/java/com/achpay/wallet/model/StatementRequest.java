package com.achpay.wallet.model;

import com.achpay.wallet.utils.CommonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StatementRequest implements Serializable {

    public StatementRequest() {
    }

    public StatementRequest(String fromDate, String toDate, int page) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.page = page;
    }

    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * sysId : APP
     * fromDate : 2018-07-01
     * toDate : 2018-07-31
     * page : 1
     * pageNum : 2
     * queryType : timeInterval
     * sign : 23232323232
     */

    @SerializedName("merchantId")
    private String merchantId = CommonUtil.getMerchantId();
    @SerializedName("sysId")
    private String sysId = CommonUtil.getSysId();
    @SerializedName("fromDate")
    private String fromDate;
    @SerializedName("toDate")
    private String toDate;
    @SerializedName("page")
    private int page;
    @SerializedName("pageNum")
    private int pageNum = 50;
    @SerializedName("queryType")
    private String queryType = "accountDetailsQuery";
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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
