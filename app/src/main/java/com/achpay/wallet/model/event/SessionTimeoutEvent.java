package com.achpay.wallet.model.event;

import java.io.Serializable;

public class SessionTimeoutEvent implements Serializable {
    private String code;

    private String message;

    public SessionTimeoutEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
