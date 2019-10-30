package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RaidenPayResponse implements Serializable {

    /**
     * id : 4
     * tokenAddress : 0xb28112409409c19C5034448AbcdF453d1D8E2b4A
     * customerAddress : 0xF65121717aBaaa87d57d06838a2f283aA60bF25A
     * proxyAddress : 0x0B14a96CbEd20797341EF18bE27Ec80b7B7e5F20
     * channelAmount : 2.9
     */

    @SerializedName("returnCode")
    private String returnCode;
    @SerializedName("returnMsg")
    private String returnMsg;
    @SerializedName("id")
    private int id;
    @SerializedName("tokenAddress")
    private String tokenAddress;
    @SerializedName("customerAddress")
    private String customerAddress;
    @SerializedName("proxyAddress")
    private String proxyAddress;
    @SerializedName("channelAmount")
    private double channelAmount;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public double getChannelAmount() {
        return channelAmount;
    }

    public void setChannelAmount(double channelAmount) {
        this.channelAmount = channelAmount;
    }
}
