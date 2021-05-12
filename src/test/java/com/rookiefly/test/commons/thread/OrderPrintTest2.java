package com.rookiefly.test.commons.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 三个线程顺序打印A、B、C十次
 */
@Slf4j
public class OrderPrintTest2 {

    private static Semaphore A = new Semaphore(1);
    private static Semaphore B = new Semaphore(0);
    private static Semaphore C = new Semaphore(0);

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        threadPool.execute(new PrintAThread());

        threadPool.execute(new PrintBThread());

        threadPool.execute(new PrintCThread());

        threadPool.shutdown();
    }

    static class PrintAThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                try {
                    A.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("A");
                i++;
                B.release();
            }
        }
    }

    static class PrintBThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                try {
                    B.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("B");
                i++;
                C.release();
            }
        }
    }

    static class PrintCThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                try {
                    C.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("C");
                i++;
                A.release();
            }
        }
    }

}
