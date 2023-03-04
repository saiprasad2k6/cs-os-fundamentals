package com.scaler.addersubtractor.lock;

public class Count {
    private volatile int value = 0;

    public int getValue() {
        return value;
    }

    public void incrementValue(int offset) {
        this.value += offset;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
