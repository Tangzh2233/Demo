package com.edu.service.quartz;

import com.edu.service.quartz.job.DailyJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Tangzhihao
 * @date 2018/3/19
 */
@Service
public class DailyJob {

    private final static Logger logger = LoggerFactory.getLogger(DailyJob.class);

    @Resource
    private DailyJobService dailyJobService;

    /**
     * @description:
     * @springboot定时任务[真鸡儿简单!]
     */
  //@Scheduled(cron = "0 0/2 * * * ?")
    public void execute(){
        logger.info("开始日报发送任务");
        try {
            dailyJobService.DailJobExecute();
        }catch (Exception e){
            logger.info("日报发送异常");
        }

    }

}
