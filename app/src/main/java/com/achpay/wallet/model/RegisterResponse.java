package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterResponse implements Serializable {

    /**
     * merchantId : a1516219f53f47708d131ce633a8af12
     * merchantName : 张亮麻辣烫O
     * merchantType : master
     * settleCryptocurrencyId : 29
     * settleCryptocurrency : USDT
     * settleCurrencyId : 840
     * settleCurrency : USD
     * sign : UYMIC6Uyq8BbY9mtCstd9H+i+BzKW2pGE6fUNUNTAY06V4JR8GTYXfFETxLEUv9RHLr+t1YqClhMsmnfWhd9tKI
     */

    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("merchantName")
    private String merchantName;
    @SerializedName("merchantType")
    private String merchantType;
    @SerializedName("settleCryptocurrencyId")
    private int settleCryptocurrencyId;
    @SerializedName("settleCryptocurrency")
    private String settleCryptocurrency;
    @SerializedName("settleCurrencyId")
    private int settleCurrencyId;
    @SerializedName("settleCurrency")
    private String settleCurrency;
    @SerializedName("sign")
    private String sign;

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

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
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

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
