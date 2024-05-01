package com.learning.pinningthread;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class PinningVirtualThreadReentrantLock {

    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {

        var lock = new ReentrantLock();

        Runnable task1 = () -> {
            for (int i = 0; i < 3; i++) {

                System.out.println(Thread.currentThread());
                lock.lock();
                try {
                    counter++;
                    threadSleep(1, ChronoUnit.MICROS);
                } finally {
                    lock.unlock();
                }

            }
            System.out.println(Thread.currentThread());
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 3; i++) {

                lock.lock();
                try {
                    counter++;
                    threadSleep(1, ChronoUnit.MICROS);
                } finally {
                    lock.unlock();
                }

            }
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

        System.out.println("# threads = " + numberOfThreads);
        System.out.println("counter = " + counter);
    }

    private static void threadSleep(int amount, ChronoUnit unit) {
        try {
            Thread.sleep(Duration.of(amount, unit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


/*

new ReentrantLock();

    lock.lock();
    try {
        counter++;
        threadSleep(1, ChronoUnit.MICROS);
    } finally {
        lock.unlock();
    }
*/
