package com.achpay.wallet.model;

import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CoinTypeResponse implements Serializable{

    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * page : 1
     * pageNo : 3
     * totalPage : 2
     * totalCount : 4
     * supportCryptosList : [{"cryptoCurrency":"BTC","cryptoCurrencyDesc":"比特币","listlogoUrl":"/pic/icon_btc.png","cryptoName":"bitcoin","cryptoProtocol":"amount","cryptoCoin":"bitcoin","fullCoinName":"Bitcoin","coinunique":1}]
     * sign : OWMzYmE0NDdjMDQxMDBlNWI3ZDdhNGJlNDA0NzczNGQ=
     */

    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("page")
    private int page;
    @SerializedName("pageNo")
    private int pageNo;
    @SerializedName("totalPage")
    private int totalPage;
    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("sign")
    private String sign;
    @SerializedName("supportCryptosList")
    private List<Cryptocurrency> supportCryptosList;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<Cryptocurrency> getSupportCryptosList() {
        return supportCryptosList;
    }

    public void setSupportCryptosList(List<Cryptocurrency> supportCryptosList) {
        this.supportCryptosList = supportCryptosList;
    }
}
