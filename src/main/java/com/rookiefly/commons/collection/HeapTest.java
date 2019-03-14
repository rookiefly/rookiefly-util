package com.rookiefly.commons.collection;

import com.github.jsonzou.jmockdata.JMockData;

import java.util.PriorityQueue;

public class HeapTest {

    public static void main(String[] args) {

        PriorityQueue<UserActivity> heap = new PriorityQueue<>();

        for (int i = 0; i < 10; i++) {
            UserActivity userActivity = JMockData.mock(UserActivity.class);
            heap.offer(userActivity);
        }

        for (UserActivity userActivity : heap) {
            System.out.println(userActivity);
        }

        System.out.println("poll heap");

        UserActivity userActivity;
        while ((userActivity = heap.poll()) != null) {
            System.out.println(userActivity);
        }
    }
}
