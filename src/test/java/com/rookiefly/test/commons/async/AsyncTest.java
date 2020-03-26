package com.rookiefly.test.commons.async;

import java.util.concurrent.CompletableFuture;

public class AsyncTest {

    public static void main(String[] args) {
        async();
        System.out.println("main done...");
    }

    public static void async(){
        CompletableFuture.runAsync(() -> {
            System.out.println("read db...");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("done");
        });
    }
}
