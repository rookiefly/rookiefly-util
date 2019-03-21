package com.rookiefly.test.commons.number;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;

/**
 * Created by rookiefly on 2015/10/27.
 */
public class ArgsTest {

    public static void main(String[] args) {

        for (String arg : args) {
            System.out.println(arg);
        }

        System.out.println(System.getProperty("appHome"));
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String test = "aaaa";
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Runtime.getRuntime().gc();
                List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
                System.out.println(garbageCollectorMXBeans.size());
                for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
                    System.out.println(garbageCollectorMXBean.getName() + "@" + garbageCollectorMXBean.getCollectionCount()
                            + "@" + garbageCollectorMXBean.getCollectionTime());
                }
            }
        }).start();
        System.out.println(threadMXBean.getThreadCount());
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        System.out.println(garbageCollectorMXBeans.size());
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            System.out.println(garbageCollectorMXBean.getName() + "@" + garbageCollectorMXBean.getCollectionCount()
                    + "@" + garbageCollectorMXBean.getCollectionTime());
        }
    }
}
