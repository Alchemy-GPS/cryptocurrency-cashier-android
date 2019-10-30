package com.achpay.wallet.model;

import com.achpay.wallet.ACHApplication;
import com.achpay.wallet.utils.CommonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CoinTypeRequest implements Serializable{
    public CoinTypeRequest(int page) {
        this.page = page;
    }

    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * sysId : APP
     * page : 1
     * pageNo : 3
     * sign : 4e6d466b4e3251314d5759
     */

    @SerializedName("merchantId")
    private String merchantId = CommonUtil.getMerchantId(ACHApplication.APPLICATION);
    @SerializedName("sysId")
    private String sysId = CommonUtil.getSysId();
    @SerializedName("page")
    private int page;
    @SerializedName("pageNo")
    private int pageNo = 50;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
