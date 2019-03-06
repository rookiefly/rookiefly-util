package com.rookiefly.test.commons.lock;

public class LockTest {

    private int count = 1;

    public static void main(String[] args) {
        LockTest lockTest = new LockTest();
        lockTest.testSync();
    }

    public void testSync() {
        synchronized (this) {
            count++;
            System.out.println(count);
        }
    }
}
