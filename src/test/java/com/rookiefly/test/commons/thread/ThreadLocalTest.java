package com.rookiefly.test.commons.thread;

import com.rookiefly.commons.threadlocal.TraceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

@Slf4j
public class ThreadLocalTest {

    private static ExecutorService executor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        executor.execute(new JobThread());
        executor.execute(new JobThread());
        executor.execute(new JobThread());
        executor.shutdown();
    }

    private static class JobThread implements Runnable {

        @Override
        public void run() {
            TraceContext.getContext().setTraceId(RandomStringUtils.random(11, true, true));
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(TraceContext.getContext().getTraceId());
        }
    }
}
