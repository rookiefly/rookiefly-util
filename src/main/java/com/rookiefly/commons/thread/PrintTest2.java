package com.rookiefly.commons.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程顺序打印A、B、C十次
 */
public class PrintTest2 {

    private static Semaphore A = new Semaphore(1);
    private static Semaphore B = new Semaphore(1);
    private static Semaphore C = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        B.acquire();
        C.acquire();

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
                System.out.println("A");
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
                System.out.println("B");
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
                System.out.println("C");
                i++;
                A.release();
            }
        }
    }

}
