package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderStatusMessage  implements Serializable{

    /**
     * blockHash :
     * blockNum : 0
     * blockTxId : gw2018091313215900001
     * confirmBlockNums : 6
     * cryptocurrency : RTT
     * cryptocurrencyId : 21
     * currency : USD
     * currencyId : 840
     * diffQuantity : 0.00000000
     * merchantId : 5229cb115c4549988d47635e821253db
     * networkFee : 0.00000000
     * orderDiffQuantityFait : 0.00
     * orderId : 2018091321215664035
     * orderQuantityFait : 0.10
     * orderReceiveQuantityFait : 0.10
     * plateformFee : 0.16423746
     * quantity : 8.21187289
     * quantityFait : 0.10
     * receiveFait : 0.10
     * receiveQuantity : 8.21187289
     * receiveTime : 2018-09-13 21:21:59
     * result : SUCCESS
     * resultMsg : 成功
     * sign : NmY1ZmNkNTFmYjIyNjRjODRhYjhkMzAyMjMyNGFlMGY=
     * sysFlowNo : gw2018091313215900001
     * terminalId : 868793036363159
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
    @SerializedName("orderDiffQuantityFait")
    private String orderDiffQuantityFait;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("orderQuantityFait")
    private String orderQuantityFait;
    @SerializedName("orderReceiveQuantityFait")
    private String orderReceiveQuantityFait;
    @SerializedName("plateformFee")
    private String plateformFee;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("quantityFait")
    private String quantityFait;
    @SerializedName("receiveFait")
    private String receiveFait;
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
    @SerializedName("terminalId")
    private String terminalId;

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

    public String getOrderDiffQuantityFait() {
        return orderDiffQuantityFait;
    }

    public void setOrderDiffQuantityFait(String orderDiffQuantityFait) {
        this.orderDiffQuantityFait = orderDiffQuantityFait;
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

    public String getOrderReceiveQuantityFait() {
        return orderReceiveQuantityFait;
    }

    public void setOrderReceiveQuantityFait(String orderReceiveQuantityFait) {
        this.orderReceiveQuantityFait = orderReceiveQuantityFait;
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

    public String getQuantityFait() {
        return quantityFait;
    }

    public void setQuantityFait(String quantityFait) {
        this.quantityFait = quantityFait;
    }

    public String getReceiveFait() {
        return receiveFait;
    }

    public void setReceiveFait(String receiveFait) {
        this.receiveFait = receiveFait;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
