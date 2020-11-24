package com.fwr.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

/**
 * @author fwr
 * @date 2020-11-24
 */
public class MyTriggerListener implements TriggerListener {

    private String name;

    public MyTriggerListener(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        System.out.println("触发器执行的时候");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        System.out.println("在trigger触发后，job将要执行时");
        // true为否决job，不执行job
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        System.out.println("错过触发时");
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        System.out.println("触发器和job完成之后");
    }
}
