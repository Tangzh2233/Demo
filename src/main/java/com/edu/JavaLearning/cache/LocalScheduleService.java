package com.edu.JavaLearning.cache;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Author: tangzh
 * @Date: 2019/5/17$ 10:29 AM$
 **/
public class LocalScheduleService {

    private int corePoolSize;
    private String scheduleName;
    private boolean isDaemon;

    private ScheduledExecutorService executor = null;

    public LocalScheduleService(int corePoolSize,String name,boolean isDaemon){
        this.corePoolSize = corePoolSize;
        this.scheduleName = name;
        this.isDaemon = isDaemon;
        initExecutor();
    }

    private void initExecutor(){
        if(executor == null){
            executor = new ScheduledThreadPoolExecutor(this.corePoolSize,
                    new BasicThreadFactory.Builder().
                            namingPattern(this.scheduleName).
                            daemon(this.isDaemon).
                            build());
        }

    }
    public ScheduledExecutorService getExecutor() {
        return executor;
    }
}

