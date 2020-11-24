package com.fwr.quartz.job;

import org.quartz.*;

/**
 * @author fwr
 * @date 2020-11-23
 */
public class Test2Job implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Trigger trigger = context.getTrigger();
        JobKey jobKey = trigger.getJobKey();
        System.out.println(jobKey);
    }
}
