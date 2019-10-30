package com.achpay.wallet.model.event;

public class LoginSuccessEvent {
    String status;

    public LoginSuccessEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
