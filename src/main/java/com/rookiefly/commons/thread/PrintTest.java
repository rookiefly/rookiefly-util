package com.rookiefly.commons.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程顺序打印A、B、C十次
 */
public class PrintTest {

    private static volatile int status = 0;

    private static Lock lock = new ReentrantLock();

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
                lock.lock();
                if (status % 3 == 0) {
                    System.out.println("A");
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    status++;
                    i++;
                }
                lock.unlock();
            }
        }
    }

    public static class PrintBThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                lock.lock();
                if (status % 3 == 1) {
                    System.out.println("B");
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    status++;
                    i++;
                }
                lock.unlock();
            }
        }
    }

    public static class PrintCThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; ) {
                lock.lock();
                if (status % 3 == 2) {
                    System.out.println("C");
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    status++;
                    i++;
                }
                lock.unlock();
            }
        }
    }

}
