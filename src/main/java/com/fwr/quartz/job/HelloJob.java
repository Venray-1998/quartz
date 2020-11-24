package com.fwr.quartz.job;

import org.quartz.*;

import java.util.Date;

/**
 * @author fwr
 * @date 2020-11-20
 */
//告诉Quartz不要并发地执行同一个job定义（这里指特定的job类）的多个实例
@DisallowConcurrentExecution
//告诉Quartz在成功执行了job类的execute方法后（没有发生任何异常），
//更新JobDetail中JobDataMap的数据，使得该job（即JobDetail）在下一次执行的时候，
//JobDataMap中是更新后的数据，而不是更新前的旧数据。
@PersistJobDataAfterExecution
public class HelloJob implements Job {

    private int i = 0;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date date = new Date();
        i++;
        System.out.println(date.toString() + i);
        System.out.println(jobExecutionContext.getJobDetail().getJobDataMap().get("student"));
        int count = jobExecutionContext.getJobDetail().getJobDataMap().getInt("count");
        System.out.println("持久存count："+count);
        count++;
        jobExecutionContext.getJobDetail().getJobDataMap().put("count", count);
        System.out.println(jobExecutionContext.getTrigger().getJobDataMap().get("message"));
        //当前任务执行时间
        System.out.println(jobExecutionContext.getFireTime());
        System.out.println(jobExecutionContext.getNextFireTime());
    }
}
