package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderCreateResponse implements Serializable {

    /**
     * sysId : APP
     * orderQuantity : 0.00566614
     * orderQuantityFait : 300.00
     * quantity : 0.00566614
     * quantityFait : 300.00
     * orderId : 8989890000006690007
     * sysOrderId : gw2018081418543200001
     * result : DEALING
     * resultMsg : 已接收
     * address : 0987654321
     * addressType : normal
     * cryptoCurrencyId : 1
     * cryptoCurrency : BTC
     * currencyId : 1
     * currency : CNY
     * exchangeRate : 52946.09424996
     * fastestFee : 0.00000200
     * halfHourFee : 0.00000200
     * hourFee : 0.00000200
     * unit : BTC
     * fastestFeeFait : 0.11
     * halfHourFeeFait : 0.00
     * hourFeeFait : 0.00
     * faitUnit : CNY
     * sign : 5a6a4669596d4e6a4f57566a4d7a63784e6d466c595459344f54526a4d6a59795a5759305a4755784e7a513d
     */

    @SerializedName("sysId")
    private String sysId;
    @SerializedName("orderQuantity")
    private String orderQuantity;
    @SerializedName("orderQuantityFait")
    private String orderQuantityFait;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("quantityFait")
    private String quantityFait;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("sysOrderId")
    private String sysOrderId;
    @SerializedName("result")
    private String result;
    @SerializedName("resultMsg")
    private String resultMsg;
    @SerializedName("address")
    private String address;
    @SerializedName("addressType")
    private String addressType;
    @SerializedName("cryptoCurrencyId")
    private int cryptoCurrencyId;
    @SerializedName("cryptoCurrency")
    private String cryptoCurrency;
    @SerializedName("currencyId")
    private int currencyId;
    @SerializedName("currency")
    private String currency;
    @SerializedName("exchangeRate")
    private String exchangeRate;
    @SerializedName("fastestFee")
    private String fastestFee;
    @SerializedName("halfHourFee")
    private String halfHourFee;
    @SerializedName("hourFee")
    private String hourFee;
    @SerializedName("unit")
    private String unit;
    @SerializedName("fastestFeeFait")
    private String fastestFeeFait;
    @SerializedName("halfHourFeeFait")
    private String halfHourFeeFait;
    @SerializedName("hourFeeFait")
    private String hourFeeFait;
    @SerializedName("faitUnit")
    private String faitUnit;
    @SerializedName("sign")
    private String sign;

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderQuantityFait() {
        return orderQuantityFait;
    }

    public void setOrderQuantityFait(String orderQuantityFait) {
        this.orderQuantityFait = orderQuantityFait;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityFait() {
        return quantityFait;
    }

    public void setQuantityFait(String quantityFait) {
        this.quantityFait = quantityFait;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSysOrderId() {
        return sysOrderId;
    }

    public void setSysOrderId(String sysOrderId) {
        this.sysOrderId = sysOrderId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public int getCryptoCurrencyId() {
        return cryptoCurrencyId;
    }

    public void setCryptoCurrencyId(int cryptoCurrencyId) {
        this.cryptoCurrencyId = cryptoCurrencyId;
    }

    public String getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
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

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getFastestFee() {
        return fastestFee;
    }

    public void setFastestFee(String fastestFee) {
        this.fastestFee = fastestFee;
    }

    public String getHalfHourFee() {
        return halfHourFee;
    }

    public void setHalfHourFee(String halfHourFee) {
        this.halfHourFee = halfHourFee;
    }

    public String getHourFee() {
        return hourFee;
    }

    public void setHourFee(String hourFee) {
        this.hourFee = hourFee;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFastestFeeFait() {
        return fastestFeeFait;
    }

    public void setFastestFeeFait(String fastestFeeFait) {
        this.fastestFeeFait = fastestFeeFait;
    }

    public String getHalfHourFeeFait() {
        return halfHourFeeFait;
    }

    public void setHalfHourFeeFait(String halfHourFeeFait) {
        this.halfHourFeeFait = halfHourFeeFait;
    }

    public String getHourFeeFait() {
        return hourFeeFait;
    }

    public void setHourFeeFait(String hourFeeFait) {
        this.hourFeeFait = hourFeeFait;
    }

    public String getFaitUnit() {
        return faitUnit;
    }

    public void setFaitUnit(String faitUnit) {
        this.faitUnit = faitUnit;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
