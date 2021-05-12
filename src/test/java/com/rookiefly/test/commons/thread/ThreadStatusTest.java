package com.rookiefly.test.commons.thread;

import static java.lang.Thread.sleep;

public class ThreadStatusTest {

    public static void main(String[] args) {
        Thread joinThread = new Thread(() -> {
            System.out.println("join thread");
            try {
                sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        joinThread.start();
        try {
            joinThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
