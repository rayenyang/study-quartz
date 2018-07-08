package com.rayenyang.studyquartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author rayenyang
 *         Date:    2018/7/8
 */
public class QuickStart {
    public static void main(String[] args) throws SchedulerException {
        
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).build();
        TriggerBuilder<CronTrigger> trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"));
        scheduler.scheduleJob(jobDetail, trigger.build());
        scheduler.start();
    }
}
