package com.rayenyang.studyquartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author rayenyang
 *         Date:    2018/7/8
 */
public class HelloJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("start job....");
    }
}
