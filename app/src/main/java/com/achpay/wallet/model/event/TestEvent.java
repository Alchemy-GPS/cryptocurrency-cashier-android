package com.achpay.wallet.model.event;

public class TestEvent {
    String text;

    public TestEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
