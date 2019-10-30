package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WSServerMessage implements Serializable {

    /**
     * blockHash : 0x724aba80211535f4f8a4d77182fe008346f0e37998078a53750101bbd23bcc35
     * blockNum : 3767283
     * blockTxId : 0x033b33fdf99960cfdc5c18f5a33a436a2d172c9618d52a0705d085460514855a
     * confirmBlockNums : 15
     * cryptocurrency : ETH
     * cryptocurrencyId : 2
     * currency : CNY
     * currencyId : 1
     * diffQuantity : 0.00000000
     * merchantId : bfa261b7f22d41c3b4cb1cfa564ae4e9
     * networkFee : 0.00000000
     * orderId : 8989890000006696
     * plateformFee : 0.00000000
     * quantity : 0.03000000
     * receiveQuantity : 0.00060000
     * receiveTime : 2018-08-04 07:35:55
     * result : CONFIRMING
     * resultMsg : 确认中
     * sign : 4d6a45314d474d774e6a51334e544d354d544d794d546c6c5957526b4e5464684f44466b4d5463794f54493d
     * sysFlowNo : gw2018080323355500001
     */

    @SerializedName("blockHash")
    private String blockHash;
    @SerializedName("blockNum")
    private int blockNum;
    @SerializedName("blockTxId")
    private String blockTxId;
    @SerializedName("confirmBlockNums")
    private int confirmBlockNums;
    @SerializedName("cryptocurrency")
    private String cryptocurrency;
    @SerializedName("cryptocurrencyId")
    private int cryptocurrencyId;
    @SerializedName("currency")
    private String currency;
    @SerializedName("currencyId")
    private int currencyId;
    @SerializedName("diffQuantity")
    private String diffQuantity;
    @SerializedName("merchantId")
    private String merchantId;
    @SerializedName("networkFee")
    private String networkFee;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("plateformFee")
    private String plateformFee;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("receiveQuantity")
    private String receiveQuantity;
    @SerializedName("receiveTime")
    private String receiveTime;
    @SerializedName("result")
    private String result;
    @SerializedName("resultMsg")
    private String resultMsg;
    @SerializedName("sign")
    private String sign;
    @SerializedName("sysFlowNo")
    private String sysFlowNo;

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public String getBlockTxId() {
        return blockTxId;
    }

    public void setBlockTxId(String blockTxId) {
        this.blockTxId = blockTxId;
    }

    public int getConfirmBlockNums() {
        return confirmBlockNums;
    }

    public void setConfirmBlockNums(int confirmBlockNums) {
        this.confirmBlockNums = confirmBlockNums;
    }

    public String getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(String cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public int getCryptocurrencyId() {
        return cryptocurrencyId;
    }

    public void setCryptocurrencyId(int cryptocurrencyId) {
        this.cryptocurrencyId = cryptocurrencyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getDiffQuantity() {
        return diffQuantity;
    }

    public void setDiffQuantity(String diffQuantity) {
        this.diffQuantity = diffQuantity;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getNetworkFee() {
        return networkFee;
    }

    public void setNetworkFee(String networkFee) {
        this.networkFee = networkFee;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlateformFee() {
        return plateformFee;
    }

    public void setPlateformFee(String plateformFee) {
        this.plateformFee = plateformFee;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSysFlowNo() {
        return sysFlowNo;
    }

    public void setSysFlowNo(String sysFlowNo) {
        this.sysFlowNo = sysFlowNo;
    }
}
