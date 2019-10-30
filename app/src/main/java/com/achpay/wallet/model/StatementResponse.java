package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StatementResponse implements Serializable {

    /**
     * merchantId : efd94acb167d45f6b218d552fafb1467
     * sysId : APP
     * details : [{"merchantId":"efd94acb167d45f6b218d552fafb1467","cryptoCurrencyId":2,"cryptoCurrency":"ETH","receiveQuantity":"0.90000000","receiveQuantityFait":"2773.51","receiveTime":"2018-08-19 10:49:00","confirmationNums":0,"accountType":"trade"},{"merchantId":"efd94acb167d45f6b218d552fafb1467","cryptoCurrencyId":2,"cryptoCurrency":"ETH","receiveQuantity":"0.04000000","receiveQuantityFait":"92.45","receiveTime":"2018-08-19 10:37:00","confirmationNums":0,"accountType":"trade"},{"merchantId":"efd94acb167d45f6b218d552fafb1467","cryptoCurrencyId":1,"cryptoCurrency":"BTC","receiveQuantity":"1.30000000","receiveQuantityFait":"68829.92","receiveTime":"2018-08-19 10:24:20","confirmationNums":0,"accountType":"crypto"},{"merchantId":"efd94acb167d45f6b218d552fafb1467","cryptoCurrencyId":2,"cryptoCurrency":"ETH","receiveQuantity":"0.03300000","receiveQuantityFait":"101.70","receiveTime":"2018-08-10 08:11:00","confirmationNums":0,"accountType":"crypto"},{"merchantId":"efd94acb167d45f6b218d552fafb1467","cryptoCurrencyId":3,"cryptoCurrency":"tBTC","receiveQuantity":"0.00094436","receiveQuantityFait":"50.00","receiveTime":"2018-08-10 07:53:45","confirmationNums":0,"accountType":"crypto"},{"merchantId":"efd94acb167d45f6b218d552fafb1467","cryptoCurrencyId":3,"cryptoCurrency":"tBTC","receiveQuantity":"0.00188871","receiveQuantityFait":"100.00","receiveTime":"2018-08-10 07:20:10","confirmationNums":0,"accountType":"crypto"}]
     * page : 1
     * pageNum : 20
     * totalPage : 1
     * totalCount : 6
     * fromDate : 2018-08-01
     * toDate : 2018-08-31
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
    private int totalPage;
    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("fromDate")
    private String fromDate;
    @SerializedName("toDate")
    private String toDate;
    @SerializedName("details")
    private List<Detail> details;

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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public static class Detail {
        /**
         * merchantId : efd94acb167d45f6b218d552fafb1467
         * cryptoCurrencyId : 2
         * cryptoCurrency : ETH
         * receiveQuantity : 0.90000000
         * receiveQuantityFait : 2773.51
         * receiveTime : 2018-08-19 10:49:00
         * confirmationNums : 0
         * accountType : trade
         */

        @SerializedName("merchantId")
        private String merchantId;
        @SerializedName("cryptoCurrencyId")
        private int cryptoCurrencyId;
        @SerializedName("cryptoCurrency")
        private String cryptoCurrency;
        @SerializedName("receiveQuantity")
        private String receiveQuantity;
        @SerializedName("receiveQuantityFait")
        private String receiveQuantityFait;
        @SerializedName("receiveTime")
        private String receiveTime;
        @SerializedName("confirmationNums")
        private int confirmationNums;
        @SerializedName("accountType")
        private String accountType;
        @SerializedName("currency")
        private String currency;
        @SerializedName("orderId")
        private String orderId;

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
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

        public String getReceiveQuantity() {
            return receiveQuantity;
        }

        public void setReceiveQuantity(String receiveQuantity) {
            this.receiveQuantity = receiveQuantity;
        }

        public String getReceiveQuantityFait() {
            return receiveQuantityFait;
        }

        public void setReceiveQuantityFait(String receiveQuantityFait) {
            this.receiveQuantityFait = receiveQuantityFait;
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

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
