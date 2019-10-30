package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    /**
     * merchantId : 917b6d4fd3dd48d8b8d6e370bf467d57
     * merchantName : Jiangjj-测试4
     * sessionId : 74a98e71-def4-4c82-9baa-293ba8094c14
     * settleCryptocurrencyId : 10
     * settleCryptocurrency : USDT
     * settleCurrencyId : 32
     * settltCurrency : USD
     */

    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("merchantName")
    private String merchantName;
    @SerializedName("sessionId")
    private String sessionId;
    @SerializedName("qianfId")
    private String qianfId;
    @SerializedName("settleCryptocurrencyId")
    private int settleCryptocurrencyId;
    @SerializedName("settleCryptocurrency")
    private String settleCryptocurrency;
    @SerializedName("settleCurrencyId")
    private int settleCurrencyId;
    @SerializedName("settltCurrency")
    private String settltCurrency;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getQianfId() {
        return qianfId;
    }

    public void setQianfId(String qianfId) {
        this.qianfId = qianfId;
    }

    public int getSettleCryptocurrencyId() {
        return settleCryptocurrencyId;
    }

    public void setSettleCryptocurrencyId(int settleCryptocurrencyId) {
        this.settleCryptocurrencyId = settleCryptocurrencyId;
    }

    public String getSettleCryptocurrency() {
        return settleCryptocurrency;
    }

    public void setSettleCryptocurrency(String settleCryptocurrency) {
        this.settleCryptocurrency = settleCryptocurrency;
    }

    public int getSettleCurrencyId() {
        return settleCurrencyId;
    }

    public void setSettleCurrencyId(int settleCurrencyId) {
        this.settleCurrencyId = settleCurrencyId;
    }

    public String getSettltCurrency() {
        return settltCurrency;
    }

    public void setSettltCurrency(String settltCurrency) {
        this.settltCurrency = settltCurrency;
    }
}
