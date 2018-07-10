package com.rayenyang.studyquartz.advance;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author yangruiheng
 *         Date:    2018/7/9
 */
public class JobDataJobTest implements Job {
	
	private String data1;
	private int data2;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("execute job....");
		System.out.println(data1);
		System.out.println(data2);
	}
	
	public String getData1() {
		return data1;
	}
	
	public void setData1(String data1) {
		this.data1 = data1;
	}
	
	public int getData2() {
		return data2;
	}
	
	public void setData2(int data2) {
		this.data2 = data2;
	}
}
