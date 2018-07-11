package com.rayenyang.studyquartz.advance;

import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author rayenyang
 *         Date:    2018/7/9
 */
public class Advance {
	static Scheduler scheduler;
	static {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("add hook");
				try {
					scheduler.shutdown(true);
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			}
		}));
	}
	
	public static void main(String[] args) throws SchedulerException, InterruptedException {
		start();
		TimeUnit.SECONDS.sleep(10);
		replaceJob();
		TimeUnit.SECONDS.sleep(10);
		replaceJob2();
	}
	
	/**
	 * will not  wait for job complete
	 */
	@Test
	public void testExitWithBigJob() throws Exception {
		JobDetail job = JobBuilder.newJob(BigJob.class)
				.withIdentity("job1", "group1")
				.build();
		Trigger trigger = TriggerBuilder.newTrigger().startNow()/*.withSchedule(CronScheduleBuilder.cronSchedule("02 * * * * ?"))*/.build();
		scheduler.scheduleJob(job, trigger);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("shutdown....");
		//test if will wait for job complete
		System.exit(0);
	}
	
	/**
	 * the job fired by old trigger will continue
	 */
	@Test
	public void testReplaceWithBigJob() throws Exception {
		JobDetail job = JobBuilder.newJob(BigJob.class)
				.withIdentity("job1", "group1")
				.build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger1", "triggerGroup1")
				.startNow().build();
		scheduler.scheduleJob(job, trigger);
		TimeUnit.SECONDS.sleep(5);
		System.out.println("replace trigger....");
		Trigger newTrigger = TriggerBuilder.newTrigger().withSchedule(
				CronScheduleBuilder.cronSchedule("* * * * * ? 2099"))
				.withIdentity("trigger1", "triggerGroup1")
				.build();
		scheduler.rescheduleJob(TriggerKey.triggerKey("trigger1", "triggerGroup1"), newTrigger);
		//block
		TimeUnit.SECONDS.sleep(60);
	}
	
	public static void start() throws SchedulerException {
		JobBuilder jobBuilder = JobBuilder.newJob(JobDataJobTest.class)
				.withIdentity("job1", "group1")
				.usingJobData("data1", "I am data1")
				.usingJobData("data2", 2);
		SimpleTrigger trigger = TriggerBuilder.newTrigger().startNow()
				.withIdentity("trigger1", "trigger-group1")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever()).build();
		scheduler.scheduleJob(jobBuilder.build(), trigger);
	}
	
	//replace job by trigger with identity
	public static void replaceJob() throws SchedulerException {
		System.out.println("111111111111111111111111111");
		SimpleTrigger trigger = TriggerBuilder.newTrigger().startNow()
				.withIdentity("trigger1", "trigger-group1")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
		Date date = scheduler.rescheduleJob(TriggerKey.triggerKey("trigger1", "trigger-group1"), trigger);
		System.out.println(date);
	}
	
	public static void replaceJob2() throws SchedulerException {
		System.out.println("222222222222222222222222222222");
//		replace again
//		SimpleTrigger trigger = TriggerBuilder.newTrigger().startNow()
//				.withIdentity("trigger1", "trigger-group1")
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
//		*********************************************************************
//	    replace job again, but by invalid cron trigger
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("* * * * * *"))
				.withIdentity("trigger1", "trigger-group1").build();
		Date date = scheduler.rescheduleJob(TriggerKey.triggerKey("trigger1", "trigger-group1"), trigger);
		System.out.println(date);
	}
}
