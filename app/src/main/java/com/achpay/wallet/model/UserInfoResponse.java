package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserInfoResponse implements Serializable {

    /**
     * accountInfos : [{"cryptocurrency":"BTC","availableBalance":"1.00000000","currencyAmount":"52946.09","settlelogoUrl":"/pic/box_btc.png"}]
     * settleInfos : [{"cryptocurrencyId":1,"cryptocurrency":"BTC","settleDays":10,"address":"1234","lowSettleAmount":"0.00000000","maxSettleAmount":"0.00000000","currencyId":1,"currency":"CNY"}]
     * totalcurrency : 61913.15
     * totalcryptocurrency : 1.16963753
     */

    @SerializedName("totalcurrency")
    private String totalcurrency;
    @SerializedName("totalcryptocurrency")
    private String totalcryptocurrency;
    @SerializedName("targetCryptoUnit")
    private String targetCryptoUnit;
    @SerializedName("targetCurrencyUnit")
    private String targetCurrencyUnit;
    @SerializedName("accountInfos")
    private List<AccountInfos> accountInfos;
    @SerializedName("settleInfos")
    private List<SettleInfos> settleInfos;

    public String getTotalcurrency() {
        return totalcurrency;
    }

    public void setTotalcurrency(String totalcurrency) {
        this.totalcurrency = totalcurrency;
    }

    public String getTotalcryptocurrency() {
        return totalcryptocurrency;
    }

    public void setTotalcryptocurrency(String totalcryptocurrency) {
        this.totalcryptocurrency = totalcryptocurrency;
    }

    public String getTargetCryptoUnit() {
        return targetCryptoUnit;
    }

    public void setTargetCryptoUnit(String targetCryptoUnit) {
        this.targetCryptoUnit = targetCryptoUnit;
    }

    public String getTargetCurrencyUnit() {
        return targetCurrencyUnit;
    }

    public void setTargetCurrencyUnit(String targetCurrencyUnit) {
        this.targetCurrencyUnit = targetCurrencyUnit;
    }

    public List<AccountInfos> getAccountInfos() {
        return accountInfos;
    }

    public void setAccountInfos(List<AccountInfos> accountInfos) {
        this.accountInfos = accountInfos;
    }

    public List<SettleInfos> getSettleInfos() {
        return settleInfos;
    }

    public void setSettleInfos(List<SettleInfos> settleInfos) {
        this.settleInfos = settleInfos;
    }

    public static class AccountInfos {
        /**
         * cryptocurrency : BTC
         * availableBalance : 1.00000000
         * currencyAmount : 52946.09
         * settlelogoUrl : /pic/box_btc.png
         */

        @SerializedName("cryptocurrency")
        private String cryptocurrency;
        @SerializedName("availableBalance")
        private String availableBalance;
        @SerializedName("currencyAmount")
        private String currencyAmount;
        @SerializedName("settlelogoUrl")
        private String settlelogoUrl;
        @SerializedName("cryptocurrencyId")
        private int cryptocurrencyId;

        public String getCryptocurrency() {
            return cryptocurrency;
        }

        public void setCryptocurrency(String cryptocurrency) {
            this.cryptocurrency = cryptocurrency;
        }

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }

        public String getCurrencyAmount() {
            return currencyAmount;
        }

        public void setCurrencyAmount(String currencyAmount) {
            this.currencyAmount = currencyAmount;
        }

        public String getSettlelogoUrl() {
            return settlelogoUrl;
        }

        public void setSettlelogoUrl(String settlelogoUrl) {
            this.settlelogoUrl = settlelogoUrl;
        }

        public int getCryptocurrencyId() {
            return cryptocurrencyId;
        }

        public void setCryptocurrencyId(int cryptocurrencyId) {
            this.cryptocurrencyId = cryptocurrencyId;
        }
    }

    public static class SettleInfos {
        /**
         * cryptocurrencyId : 1
         * cryptocurrency : BTC
         * settleDays : 10
         * address : 1234
         * lowSettleAmount : 0.00000000
         * maxSettleAmount : 0.00000000
         * currencyId : 1
         * currency : CNY
         */

        @SerializedName("cryptocurrencyId")
        private int cryptocurrencyId;
        @SerializedName("cryptocurrency")
        private String cryptocurrency;
        @SerializedName("settleDays")
        private int settleDays;
        @SerializedName("address")
        private String address;
        @SerializedName("lowSettleAmount")
        private String lowSettleAmount;
        @SerializedName("maxSettleAmount")
        private String maxSettleAmount;
        @SerializedName("currencyId")
        private int currencyId;
        @SerializedName("currency")
        private String currency;

        public int getCryptocurrencyId() {
            return cryptocurrencyId;
        }

        public void setCryptocurrencyId(int cryptocurrencyId) {
            this.cryptocurrencyId = cryptocurrencyId;
        }

        public String getCryptocurrency() {
            return cryptocurrency;
        }

        public void setCryptocurrency(String cryptocurrency) {
            this.cryptocurrency = cryptocurrency;
        }

        public int getSettleDays() {
            return settleDays;
        }

        public void setSettleDays(int settleDays) {
            this.settleDays = settleDays;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLowSettleAmount() {
            return lowSettleAmount;
        }

        public void setLowSettleAmount(String lowSettleAmount) {
            this.lowSettleAmount = lowSettleAmount;
        }

        public String getMaxSettleAmount() {
            return maxSettleAmount;
        }

        public void setMaxSettleAmount(String maxSettleAmount) {
            this.maxSettleAmount = maxSettleAmount;
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
    }
}
