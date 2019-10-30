package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrencyToCoinResponse implements Serializable {

    /**
     * quantity : 1.09837200
     * sign : OTNmMjhkMGM1OThiOTAxZDNiOWU5YjIzNGU0ODU2YWE=
     */

    @SerializedName("quantity")
    private String quantity;
    @SerializedName("exchangeRate")
    private String exchangeRate;
    @SerializedName("sign")
    private String sign;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
