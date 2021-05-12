package com.rookiefly.test.commons.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * cyclicbarrier测试
 */
@Slf4j
public class CyclicBarrierTest {

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> log.info("所有写入数据任务完毕，开始执行其他任务..."));

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(new WriteThread("job1", 3000));
        executorService.execute(new WriteThread("job2", 4000));
        executorService.execute(new WriteThread("job3", 5000));

        executorService.shutdown();
    }

    public static class WriteThread implements Runnable {

        private String jobName;

        private long timeout;

        public WriteThread(String jobName, long timeout) {
            this.jobName = jobName;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            log.info(jobName + "开始写入数据");
            try {
                sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(jobName + "写入数据完成，等待其他线程...");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}
