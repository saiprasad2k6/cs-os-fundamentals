package com.scaler.addersubtractor.executor;

public class Count {
    private volatile int value = 0;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
