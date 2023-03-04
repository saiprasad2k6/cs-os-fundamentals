package com.scaler.addersubtractor.synchronize.block;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Adder implements Runnable {
    @NonNull
    private Count count;

    @Override
    public void run() {

        for (int i = 1; i <= 100; ++i) {
            synchronized (count) {
                System.out.println("Count from Adder for i=" + i + " is " + count.getValue());
                count.setValue(count.getValue() + 1);
            }
        }
    }
}
