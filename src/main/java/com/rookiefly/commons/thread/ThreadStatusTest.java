package com.rookiefly.commons.thread;

public class ThreadStatusTest {

    public static void main(String[] args) {
        Thread joinThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("join thread");
                try {
                    Thread.currentThread().sleep(1000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
