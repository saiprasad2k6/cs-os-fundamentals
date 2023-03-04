package com.scaler.addersubtractor.synchronize.method;

public class Count {
    private volatile int value = 0;
    public int getValue() {
        return value;
    }

    public synchronized void incrementValue(int offset) {
        this.value += offset;
    }
}
