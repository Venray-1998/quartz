package com.fwr.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * @author fwr
 * @date 2020-11-24
 */
public class MyJobListener implements JobListener {

    private String name;

    public MyJobListener(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        //在jobToBeExecuted方法之前执行一次，在job和jobWasExecuted方法之间执行一次
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        System.out.println("jobListener在job执行前");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        System.out.println("job即将执行，但被triggerListener否决时调用该方法，没有则调用jobToBeExecuted");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        System.out.println("jobListener在job执行后");
    }
}
