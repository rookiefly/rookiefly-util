package com.rookiefly.commons.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * 相同的一个 ZeroEvenOdd 类实例将会传递给三个不同的线程：
 * <p>
 * 线程 A 将调用 zero()，它只输出 0 。
 * 线程 B 将调用 even()，它只输出偶数。
 * 线程 C 将调用 odd()，它只输出奇数。
 * 每个线程都有一个 printNumber 方法来输出一个整数。请修改给出的代码以输出整数序列 010203040506... ，其中序列的长度必须为 2n。
 * <p>
 * 示例 1：
 * <p>
 * 输入：n = 2
 * 输出："0102"
 * 说明：三条线程异步执行，其中一个调用 zero()，另一个线程调用 even()，最后一个线程调用odd()。正确的输出为 "0102"。
 * 示例 2：
 * <p>
 * 输入：n = 5
 * 输出："0102030405"
 */
public class ZeroEvenOdd {

    private Semaphore zeroSemaphore, evenSemaphore, oddSemaphore;

    private int n;

    public ZeroEvenOdd(int n) {
        zeroSemaphore = new Semaphore(1);
        evenSemaphore = new Semaphore(0);
        oddSemaphore = new Semaphore(0);
        this.n = n;
    }

    /**
     * 仅打印出0
     */
    public void zero(IntConsumer printNumber) {
        for (int i = 1; i <= n; i++) {
            try {
                zeroSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printNumber.accept(0);
            if ((i & 1) == 0) {
                evenSemaphore.release();
            } else {
                oddSemaphore.release();
            }
        }
    }

    /**
     * 仅打印出偶数
     */
    public void even(IntConsumer printNumber) {
        for (int i = 2; i <= n; i = i + 2) {
            try {
                evenSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printNumber.accept(i);
            zeroSemaphore.release();
        }
    }

    /**
     * 仅打印出奇数
     */
    public void odd(IntConsumer printNumber) {
        for (int i = 1; i <= n; i = i + 2) {
            try {
                oddSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printNumber.accept(i);
            zeroSemaphore.release();
        }
    }

    public static void main(String[] args) {

        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(6);

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        fixedThreadPool.submit(() -> zeroEvenOdd.zero(System.out::print));

        fixedThreadPool.submit(() -> zeroEvenOdd.even(System.out::print));

        fixedThreadPool.submit(() -> zeroEvenOdd.odd(System.out::print));

        fixedThreadPool.shutdown();
    }
}
