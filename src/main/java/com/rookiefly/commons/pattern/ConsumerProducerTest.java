package com.rookiefly.commons.pattern;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerProducerTest {

    public static int count = 0;

    public static final int FULL = 10;

    public static final Object LOCK = new Object();

    public static ExecutorService consumerExecutorService = Executors.newFixedThreadPool(3);

    public static ExecutorService producerExecutorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            producerExecutorService.submit(new ProducerThread());
        }

        for (int i = 0; i < 3; i++) {
            consumerExecutorService.submit(new ConsumerThread());
        }

        producerExecutorService.shutdown();

        consumerExecutorService.shutdown();

    }

    /**
     * 消费者线程
     */
    public static class ConsumerThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (LOCK) {
                    while (count == 0) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " wait not empty");
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println("Thread: " + Thread.currentThread().getName() + "消费者消费，目前总共有" + count);
                    LOCK.notifyAll();
                }
            }
        }
    }

    /**
     * 生产者线程
     */
    public static class ProducerThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.currentThread().sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (LOCK) {
                    while (count == FULL) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " wait not full");
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println("Thread: " + Thread.currentThread().getName() + "生产者生产，目前总共有" + count);
                    LOCK.notifyAll();
                }
            }
        }
    }
}
