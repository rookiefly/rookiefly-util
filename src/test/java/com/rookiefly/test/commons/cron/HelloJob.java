package com.rookiefly.test.commons.cron;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by rookiefly on 2016/3/16.
 */
public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("do job...");
        System.out.println(jobExecutionContext);
    }
}
