package com.achpay.wallet.model.event;

import java.io.Serializable;

public class RateEvent implements Serializable {
    private double rate;

    public RateEvent(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
