package com.fwr.quartz;

import com.fwr.quartz.entity.Student;
import com.fwr.quartz.job.HelloJob;
import com.fwr.quartz.job.Test2Job;
import com.fwr.quartz.listener.MyJobListener;
import com.fwr.quartz.listener.MyScheduleListener;
import com.fwr.quartz.listener.MyTriggerListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.calendar.CronCalendar;
import org.quartz.impl.calendar.HolidayCalendar;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;


@SpringBootTest
@Slf4j
class QuartzApplicationTests {
    
    @Test
    void contextLoads() throws Exception {
        log.error("测试slf4j");
        JobDataMap map = new JobDataMap();
        Student student = new Student();
        student.setId(1);
        map.put("student", student);
        map.put("count", 1);
        // 任务实例(每执行一次创建一个新的HelloJob实例,
        // job必须有一个无参的构造函数（当使用默认的JobFactory时）；
        // 另一个后果是，在job类中，不应该定义有状态的数据属性，因为在job的多次执行中，这些属性的值不会保留。)
        // 靠jobData保存信息
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity("job1", "JGroup1")
                .usingJobData(map)
                .build();
        //五一劳动节
        AnnualCalendar holidays = new AnnualCalendar();
        Calendar laborDay = new GregorianCalendar(2015, 4, 1);
        holidays.setDayExcluded(laborDay, true);//排除的日期，如果设置为false则为包含
        //CronCalendar cronCalendar = new CronCalendar("* * * * * ?"); 自定义规则的假期可以用cron表达式
        //调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //绑定休假日期
        scheduler.addCalendar("myHolidays", holidays, false, false);

        //触发器
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "tGroup1")
                .startNow()
                //频率触发器
                .withSchedule(simpleSchedule()
                        .repeatForever()
                        .withIntervalInSeconds(5)
                        .withRepeatCount(3))
                //绑定休假日期，休假时间内不触发
                .modifiedByCalendar("myHolidays")
                .usingJobData("message","b")
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();
        Thread.sleep(10000);
        scheduler.shutdown();
    }

    @Test
    void test2() throws Exception {
        JobDetail job = JobBuilder.newJob(Test2Job.class)
                .withIdentity("job1", "JGroup1")
                .build();

        Date start = new Date();
        start.setTime(start.getTime() + 1000);
        Date end = new Date();
        end.setTime(start.getTime() + 5000);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "tGroup1")
                .startAt(start)
                .endAt(end)
                //日历触发，在指定日期内执行，具体规则百度cron生成器
                .withSchedule(cronSchedule("0/2 * * * * ?"))
                .build();

        //Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();

        //全局job监听
        scheduler.getListenerManager().addJobListener(new MyJobListener("job监听器"), EverythingMatcher.allJobs());
        //局部监听
        //scheduler.getListenerManager().addJobListener(new MyJobListener(), KeyMatcher.keyEquals(new JobKey("job1","JGroup1")));
        //scheduler.getListenerManager().addJobListener(new MyJobListener(), GroupMatcher.groupEquals("JGroup"));

        //全局trigger监听
        scheduler.getListenerManager().addTriggerListener(new MyTriggerListener("trigger监听器"), EverythingMatcher.allTriggers());
        //scheduler监听
        scheduler.getListenerManager().addSchedulerListener(new MyScheduleListener());

        scheduler.scheduleJob(job, trigger);
        scheduler.start();
        Thread.sleep(3000);
        scheduler.shutdown();
    }

}
