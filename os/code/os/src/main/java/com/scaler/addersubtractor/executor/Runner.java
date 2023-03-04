package com.scaler.addersubtractor.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        Count count = new Count();

        Adder adder = new Adder(count);
        Subtractor subtractor = new Subtractor(count);

        //New Cached Thread Pool will give you a Executor Service Interface. You will need to add only instances implementing `Runnable`
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(adder);
        executor.execute(subtractor);

        executor.shutdown(); // The threads in the pool will exist until it is explicitly shutdown.

        if (executor.awaitTermination(100, TimeUnit.SECONDS)) {
            System.out.println(count.getValue());
        } else {
            System.out.println("Something wrong happened");
        }
    }
}
