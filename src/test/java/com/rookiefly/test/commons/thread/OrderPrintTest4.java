package com.rookiefly.test.commons.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * 三个线程顺序打印A、B、C十次
 */
@Slf4j
public class OrderPrintTest4 {

    private static AtomicInteger status = new AtomicInteger();

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        threadPool.execute(new PrintAThread());

        threadPool.execute(new PrintBThread());

        threadPool.execute(new PrintCThread());

        threadPool.shutdown();
    }

    public static class PrintAThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                if (status.get() % 3 == 0) {
                    log.info("A");
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    status.incrementAndGet();
                    i++;
                }
            }
        }
    }

    public static class PrintBThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                if (status.get() % 3 == 1) {
                    log.info("B");
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    status.incrementAndGet();
                    i++;
                }
            }
        }
    }

    public static class PrintCThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                if (status.get() % 3 == 2) {
                    log.info("C");
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    status.incrementAndGet();
                    i++;
                }
            }
        }
    }

}
