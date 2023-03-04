package com.scaler.addersubtractor.lock;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;

@RequiredArgsConstructor
public class Adder implements Runnable {
    @NonNull
    private Count count;
    @NonNull
    private Lock lock;

    @Override
    public void run() {

        for (int i = 1; i <= 100; ++i) {
            lock.lock();

            System.out.println("Count from Adder for i=" + i + " is " + count.getValue());
            count.setValue(count.getValue() + 1);

            lock.unlock();
        }

    }
}
