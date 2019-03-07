package com.rookiefly.commons.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author rookiefly
 * 线程的测试
 */

public class ThreadPoolTest {

    public static void main(String[] args) {
        /**
         * 5个线程的线程池
         */
        ExecutorService scheduler = Executors.newFixedThreadPool(5);
        scheduler.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("done");
            }
        });
    }
}
