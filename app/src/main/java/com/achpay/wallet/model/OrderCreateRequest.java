package com.achpay.wallet.model;

import com.achpay.wallet.utils.CommonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderCreateRequest implements Serializable {

    /**
     * merchantId : bfa261b7f22d41c3b4cb1cfa564ae4e9
     * sysId : APP
     * orderId : 8989890000006696
     * orderQuantity : 0.05
     * quantity : 0.03
     * cryptoCurrencyId : 2
     * exchangeRate : 349587
     * currencyId : 1
     * timezone : GMT+8:00
     * isSales : Y
     * addressType : normal
     * salseList : [{"salesKind":"subtract","salesType":"discount","salesValue":"0.3"}]
     * sign : 4e6d466b4e3251314d5759325a4468685a5467784f44426a597a59324d474d78595445794e5445325a6d493d
     */

    @SerializedName("merchantId")
    private String merchantId = CommonUtil.getMerchantId();
    @SerializedName("sysId")
    private String sysId = CommonUtil.getSysId();
    @SerializedName("terminalId")
    private String terminalId;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("orderQuantityFait")
    private String orderQuantityFait;
    @SerializedName("currencyId")
    private String currencyId;
    @SerializedName("cryptoCurrencyId")
    private String cryptoCurrencyId;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("isSales")
    private String isSales;
    @SerializedName("addressType")
    private String addressType;
    @SerializedName("sign")
    private String sign;
    @SerializedName("salesList")
    private List<Sales> salesList;

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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderQuantityFait() {
        return orderQuantityFait;
    }

    public void setOrderQuantityFait(String orderQuantityFait) {
        this.orderQuantityFait = orderQuantityFait;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCryptoCurrencyId() {
        return cryptoCurrencyId;
    }

    public void setCryptoCurrencyId(String cryptoCurrencyId) {
        this.cryptoCurrencyId = cryptoCurrencyId;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIsSales() {
        return isSales;
    }

    public void setIsSales(String isSales) {
        this.isSales = isSales;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<Sales> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<Sales> salesList) {
        this.salesList = salesList;
    }

    public static class Sales {
        /**
         * salesKind : subtract
         * salesType : discount
         * salesValue : 0.3
         */

        @SerializedName("salesKind")
        private String salesKind;
        @SerializedName("salesType")
        private String salesType;
        @SerializedName("salesValue")
        private String salesValue;

        public String getSalesKind() {
            return salesKind;
        }

        public void setSalesKind(String salesKind) {
            this.salesKind = salesKind;
        }

        public String getSalesType() {
            return salesType;
        }

        public void setSalesType(String salesType) {
            this.salesType = salesType;
        }

        public String getSalesValue() {
            return salesValue;
        }

        public void setSalesValue(String salesValue) {
            this.salesValue = salesValue;
        }
    }
}
