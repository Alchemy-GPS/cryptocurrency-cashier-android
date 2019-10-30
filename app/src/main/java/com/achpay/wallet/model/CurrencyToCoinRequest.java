package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrencyToCoinRequest implements Serializable {

    public CurrencyToCoinRequest(String cryptoCurrencyId, String currencyId, String amount) {
        this.cryptoCurrencyId = cryptoCurrencyId;
        this.currencyId = currencyId;
        this.amount = amount;
    }

    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * type : tocryptocurrency
     * cryptoCurrencyId : 1
     * currencyId : 1
     * amount : 56396.00
     * sign : 23232323232
     */

    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("type")
    private String type = "tocryptocurrency";
    @SerializedName("cryptoCurrencyId")
    private String cryptoCurrencyId;
    @SerializedName("currencyId")
    private String currencyId;
    @SerializedName("amount")
    private String amount;
    @SerializedName("sign")
    private String sign;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCryptoCurrencyId() {
        return cryptoCurrencyId;
    }

    public void setCryptoCurrencyId(String cryptoCurrencyId) {
        this.cryptoCurrencyId = cryptoCurrencyId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
