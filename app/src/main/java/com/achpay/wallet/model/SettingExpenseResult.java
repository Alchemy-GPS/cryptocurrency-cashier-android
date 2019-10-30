package com.achpay.wallet.model;

import java.io.Serializable;

public class SettingExpenseResult implements Serializable {
    private boolean isCash;
    private boolean isDiscount;
    private String amount;

    public boolean isCash() {
        return isCash;
    }

    public void setCash(boolean cash) {
        isCash = cash;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
