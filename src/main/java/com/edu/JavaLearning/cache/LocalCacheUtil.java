package com.edu.JavaLearning.cache;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: tangzh
 * @Date: 2018/9/30$ 下午4:32$
 **/
public class LocalCacheUtil {

    private static  Map<String,Object> data;
    private static final int DEFAULT_SIZE = 1000;
    private final static int DEFAULT_TIME = 3600;
    private final static ScheduledExecutorService executorService;

    static {
        data = new ConcurrentHashMap<>(DEFAULT_SIZE);
        executorService = new ScheduledThreadPoolExecutor(10);
    }

    public static void put(String key,Object value){
        data.put(key,value);
        executorService.schedule(timerTaskFactory(key),DEFAULT_TIME, TimeUnit.SECONDS);
    }

    private static TimerTask timerTaskFactory(String key){

        TimerTask task  = new TimerTask() {
            @Override
            public void run() {
                data.remove(key);
            }
        };
        return task;
    }
}
