package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

public class SysCurrencyResponse {

    /**
     * id : 7
     * currencyId : 1
     * currency : CNY
     * currencyDesc : 人民币
     * isAvailable : true
     */

    @SerializedName("id")
    private int id;
    @SerializedName("currencyId")
    private int currencyId;
    @SerializedName("currency")
    private String currency;
    @SerializedName("currencyDesc")
    private String currencyDesc;
    @SerializedName("isAvailable")
    private boolean isAvailable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
