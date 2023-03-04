package com.scaler.addersubtractor.lock;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;

@RequiredArgsConstructor
public class Subtractor implements Runnable {
    @NonNull
    private Count count;
    @NonNull
    private Lock lock;

    @Override
    public void run() {

        for (int i = 1; i <= 100; ++i) {
            lock.lock();

            System.out.println("Count from Subtractor for i=" + i + " is " + count.getValue());
            count.setValue(count.getValue() - i);

            lock.unlock();
        }

    }
}