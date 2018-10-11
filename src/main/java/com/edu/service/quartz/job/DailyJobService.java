package com.edu.service.quartz.job;

import com.edu.common.EWarnChannel;
import com.edu.dao.mapper.cat.AlertMapper;
import com.edu.dao.mapper.cat.ProjectMapper;
import com.edu.service.quartz.job.Thread.ReqAlarmThread;
import com.edu.util.DateUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Tangzhihao
 * @date 2018/3/19
 */
@Service
public class DailyJobService {

    private final static Logger logger = LoggerFactory.getLogger(DailyJobService.class);
    private static ExecutorService executorService;
    private static List<String> channels = new ArrayList<>();
    @Resource
    private AlertMapper alertMapper;
    @Resource
    private ProjectMapper projectMapper;

    static {
        channels.add("Email");
        channels.add("WeiXin");
        channels.add("SMS");
    }

    public void dailJobExecute(){

        String strDate = DateUtil.forMatDate(new Date(),"yyyy-MM-dd");
        //strDate = "2018-04-03";
        Set<String> domains = alertMapper.selectDomainByAlertTime(strDate);
        //cat自身报警没有日报
        if(domains.contains("cat")){
            domains.remove("cat");
        }

        if(null==domains||domains.size()==0){
            return;
        }
        try {
            sendAction(domains, strDate);
        }catch (Exception e){
            logger.info("请求alarmcore异常");
        }
    }

    private void sendAction(Set<String> domains,String strDate){
        logger.info("进入sendAction方法，参数: "+domains +"+"+ strDate);
        if(executorService==null){
            executorService = createThreadPool(10);
        }
        for (String domain:domains){
            try {
                executorService.execute(new ReqAlarmThread(domain,strDate, EWarnChannel.E_MAIL.getCode(),alertMapper,projectMapper));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*for (String domain:domains){
            executorService.execute(new ReqAlarmThread(domain,strDate,EWarnChannel.E_SMS.getCode(),alertMapper,projectMapper));
        }*/
        /*for (String domain:domains){
            executorService.execute(new ReqAlarmThread(domain,strDate, EWarnChannel.E_WEIXIN.getCode(),alertMapper,projectMapper));
        }*/
    }
    private ExecutorService createThreadPool(int size){
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        return new ThreadPoolExecutor(size,size,0L, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>(1024),threadFactory,new ThreadPoolExecutor.AbortPolicy());
    }

}
