package com.alibaba.chaosblade.box.dao.scheduler.cron;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Configuration;

/**
 * @author haibin
 *
 *
 */
@Configuration
public class CronConfiguration {

    public static class MyJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("hello");
        }
    }

}
