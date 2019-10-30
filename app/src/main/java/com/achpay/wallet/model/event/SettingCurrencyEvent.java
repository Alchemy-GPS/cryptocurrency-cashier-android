package com.achpay.wallet.model.event;

import java.io.Serializable;

public class SettingCurrencyEvent implements Serializable {

    private String message;

    private String currencyType;

    public SettingCurrencyEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
