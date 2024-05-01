package com.learning;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class BlockingThread {
    public static void main(String[] args) throws InterruptedException {

        Runnable task1 = () -> {
            System.out.println(Thread.currentThread());
            threadSleep(10, ChronoUnit.MICROS);
            System.out.println(Thread.currentThread());
            threadSleep(10, ChronoUnit.MICROS);
            System.out.println(Thread.currentThread());
            threadSleep(10, ChronoUnit.MICROS);
            System.out.println(Thread.currentThread());
        };

        Runnable task2 = () -> {
            threadSleep(10, ChronoUnit.MICROS);
            threadSleep(10, ChronoUnit.MICROS);
            threadSleep(10, ChronoUnit.MICROS);
        };
        
        int numberOfThreads = 100;
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < numberOfThreads; i++) {
            var thread = 
                    i == 0 ?
                            Thread.ofVirtual().unstarted(task1) :
                            Thread.ofVirtual().unstarted(task2);
            threads.add(thread);
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    private static void threadSleep(int amount, ChronoUnit unit) {
        try {
            Thread.sleep(Duration.of(amount, unit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
