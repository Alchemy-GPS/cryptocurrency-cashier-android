package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {

    /**
     * merchantId : 9d421ee24e39405abfd085302b24413f
     * sysId : APP
     * details : [{"quantity":"3.00000000","orderId":"8989890","sysOrderId":"gw2018072510330100001","result":"DEALING","resultMsg":"已接收","address":"dfdfdfdfd","cryptoCurrencyId":1,"receiveQuantity":"0.00000000","receiveTime":"2018-07-25 12:12:33","confirmationNums":0,"plateformfee":"0.00000000","plateformfeetype":"cryptocurrency"},"..."]
     * page : 1
     * pageNum : 2
     */

    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("sysId")
    private String sysId;
    @SerializedName("page")
    private int page;
    @SerializedName("pageNum")
    private int pageNum;
    @SerializedName("totalPage")
    private String totalPage;
    @SerializedName("totalCount")
    private String totalCount;
    @SerializedName("details")
    private List<OrderDetail> orderDetails;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public static class OrderDetail {

        /**
         * quantity : 3.00000000
         * orderId : 8989890
         * sysOrderId : gw2018072510330100001
         * result : DEALING
         * resultMsg : 已接收
         * address : dfdfdfdfd
         * cryptoCurrencyId : 1
         * receiveQuantity : 0.00000000
         * receiveTime : 2018-07-25 12:12:33
         * confirmationNums : 0
         * plateformfee : 0.00000000
         * plateformfeetype : cryptocurrency
         */

        @SerializedName("orderQuantity")
        private String orderQuantity;//原始订单金额
        @SerializedName("orderQuantityFait")
        private String orderQuantityFait;
        @SerializedName("quantity")
        private String quantity;//优惠后的金额
        @SerializedName("quantityFait")
        private String quantityFait;
        @SerializedName("receiveQuantity")
        private String receiveQuantity;//实收金额
        @SerializedName("receiveQuantityFait")
        private String receiveQuantityFait;
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
        @SerializedName("cryptoCurrencyId")
        private int cryptoCurrencyId;
        @SerializedName("cryptoCurrency")
        private String cryptoCurrency;
        @SerializedName("currencyId")
        private int currencyId;
        @SerializedName("currency")
        private String currency;
        @SerializedName("receiveTime")
        private String receiveTime;
        @SerializedName("confirmationNums")
        private int confirmationNums;
        @SerializedName("plateformfee")
        private String plateformfee;
        @SerializedName("plateformfeetype")
        private String plateformfeetype;

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
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

        public int getCryptoCurrencyId() {
            return cryptoCurrencyId;
        }

        public void setCryptoCurrencyId(int cryptoCurrencyId) {
            this.cryptoCurrencyId = cryptoCurrencyId;
        }

        public String getReceiveQuantity() {
            return receiveQuantity;
        }

        public void setReceiveQuantity(String receiveQuantity) {
            this.receiveQuantity = receiveQuantity;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public int getConfirmationNums() {
            return confirmationNums;
        }

        public void setConfirmationNums(int confirmationNums) {
            this.confirmationNums = confirmationNums;
        }

        public String getPlateformfee() {
            return plateformfee;
        }

        public void setPlateformfee(String plateformfee) {
            this.plateformfee = plateformfee;
        }

        public String getPlateformfeetype() {
            return plateformfeetype;
        }

        public void setPlateformfeetype(String plateformfeetype) {
            this.plateformfeetype = plateformfeetype;
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

        public String getQuantityFait() {
            return quantityFait;
        }

        public void setQuantityFait(String quantityFait) {
            this.quantityFait = quantityFait;
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

        public String getReceiveQuantityFait() {
            return receiveQuantityFait;
        }

        public void setReceiveQuantityFait(String receiveQuantityFait) {
            this.receiveQuantityFait = receiveQuantityFait;
        }
    }
}
