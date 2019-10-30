package com.achpay.wallet.model;

import java.io.Serializable;

/**
 * 特指从客户端发出的消息
 */
public class WSClientMessage implements Serializable{
    private int code;
    private String message;

    public WSClientMessage() {
    }

    public WSClientMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
