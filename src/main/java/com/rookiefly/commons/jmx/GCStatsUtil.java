package com.rookiefly.commons.jmx;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @create: 2018-12-05
 **/
public class GCStatsUtil {

    private static final String[] youngGenCollectorNames = {
            // Oracle (Sun) HotSpot
            // -XX:+UseSerialGC
            "Copy",
            // -XX:+UseParNewGC
            "ParNew",
            // -XX:+UseParallelGC
            "PS Scavenge",

            // Oracle (BEA) JRockit
            // -XgcPrio:pausetime
            "Garbage collection optimized for short pausetimes Young Collector",
            // -XgcPrio:throughput
            "Garbage collection optimized for throughput Young Collector",
            // -XgcPrio:deterministic
            "Garbage collection optimized for deterministic pausetimes Young Collector"};

    private static final String[] oldGenCollectorNames = {
            // Oracle (Sun) HotSpot
            // -XX:+UseSerialGC
            "MarkSweepCompact",
            // -XX:+UseParallelGC and (-XX:+UseParallelOldGC or -XX:+UseParallelOldGCCompacting)
            "PS MarkSweep",
            // -XX:+UseConcMarkSweepGC
            "ConcurrentMarkSweep",

            // Oracle (BEA) JRockit
            // -XgcPrio:pausetime
            "Garbage collection optimized for short pausetimes Old Collector",
            // -XgcPrio:throughput
            "Garbage collection optimized for throughput Old Collector",
            // -XgcPrio:deterministic
            "Garbage collection optimized for deterministic pausetimes Old Collector"};

    public static void getGCCount() {
        long youngGCCount = 0;
        long fullGCCount = 0;
        List<GarbageCollectorMXBean> lisfOfGCMbean = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : lisfOfGCMbean) {
            if (Arrays.asList(youngGenCollectorNames).contains(garbageCollectorMXBean.getName())) {
                youngGCCount = youngGCCount + garbageCollectorMXBean.getCollectionCount();
            } else if (Arrays.asList(oldGenCollectorNames).contains(garbageCollectorMXBean.getName())) {
                fullGCCount = fullGCCount + garbageCollectorMXBean.getCollectionCount();
            }
        }

        System.out.println(String.format("youngGCCount : %s, fullGCCount : %s", youngGCCount, fullGCCount));
    }

    public static void main(String[] args) {
        List <String> buffer = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            buffer.add("String" + i);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    getGCCount();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
