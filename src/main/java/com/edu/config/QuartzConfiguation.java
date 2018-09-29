package com.edu.config;


import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author Tangzhihao
 * @date 2018/3/23
 */

public class QuartzConfiguation {


    /**
     * @description:创建定时任务调度器
     */
    @Bean(name = "SchedulerFactoryBean")
    public SchedulerFactoryBean createSchedulerFactoryBean(Trigger jobTrigger){
        SchedulerFactoryBean schedulerBean = new SchedulerFactoryBean();
        //用于quartz集群，QuartzScheduler 启动时更新己存在的Job
        schedulerBean.setOverwriteExistingJobs(true);
        //设置1s延时启动
        schedulerBean.setStartupDelay(1);
        //挂载Trigger触发器
        schedulerBean.setTriggers(jobTrigger);
        return schedulerBean;
    }

    /**
     * @description:创建触发器Trigger
     */
    @Bean(name = "CronTriggerFactoryBean")
    public CronTriggerFactoryBean createTrigger(JobDetail jobDetail){
        CronTriggerFactoryBean triggerBean = new CronTriggerFactoryBean();
        //设置触发条件
        triggerBean.setCronExpression("0 0/1 0/2 * * ? *");
        //设置具体执行任务
        triggerBean.setJobDetail(jobDetail);
        return triggerBean;
    }

    /**
     * @description:创建具体定时任务jobDetail
     */
    @Bean(name = "jobDetail")
    public MethodInvokingJobDetailFactoryBean createJobDetailFactoryBean(ScheduledTask task){
        MethodInvokingJobDetailFactoryBean jobDetailBean = new MethodInvokingJobDetailFactoryBean();
        //是否并发执行(上一个任务执行完之后才会执行新任务)
        jobDetailBean.setConcurrent(false);
        jobDetailBean.setTargetObject(task);
        jobDetailBean.setTargetMethod("method");
        return  jobDetailBean;
    }
}
