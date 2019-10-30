package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CoinToCurrencyResponse implements Serializable {

    /**
     * amount : 52946.09
     * sign : YTY4NjQ2YzY5ZjRjMmRiY2U5OGY5Y2UzYjEzZDc0NGI=
     */

    @SerializedName("amount")
    private String amount;
    @SerializedName("exchangeRate")
    private String exchangeRate;
    @SerializedName("sign")
    private String sign;

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

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
