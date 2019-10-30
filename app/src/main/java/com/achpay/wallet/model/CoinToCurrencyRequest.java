package com.achpay.wallet.model;

import com.achpay.wallet.utils.CommonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CoinToCurrencyRequest implements Serializable{

    public CoinToCurrencyRequest(String cryptoCurrencyId, String currencyId, String quantity) {
        this.cryptoCurrencyId = cryptoCurrencyId;
        this.currencyId = currencyId;
        this.quantity = quantity;
    }

    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * type : tocurrency
     * cryptoCurrencyId : 1
     * currencyId : 1
     * quantity : 1
     * sign : 23232323232
     */

    @SerializedName("merchantId")
    private String merchantId = CommonUtil.getMerchantId();
    @SerializedName("type")
    private String type = "tocurrency";
    @SerializedName("cryptoCurrencyId")
    private String cryptoCurrencyId;
    @SerializedName("currencyId")
    private String currencyId;
    @SerializedName("quantity")
    private String quantity;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
