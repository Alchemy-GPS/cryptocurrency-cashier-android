package com.achpay.wallet.model.event;

import java.io.Serializable;

public class RateCheckEvent implements Serializable {
    private String pageName;

    public RateCheckEvent(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
