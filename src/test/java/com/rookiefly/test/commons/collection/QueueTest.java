package com.rookiefly.test.commons.collection;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueTest {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue();
        concurrentLinkedQueue.offer("rookiefly");
    }
}
