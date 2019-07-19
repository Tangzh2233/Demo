package com.edu.JavaLearning.cache;

import com.edu.util.DateUtil;
import com.edu.util.HexUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 简单的本地缓存工具类:问题->因为内存问题,需要限制数据量。所以这里重点在于数据的清除。
 * 数据清除分主动|被动。主动:启动后台线程,定期清理过期key 被动:做查询操作时，判断过期时间，过期则清除。
 * @Author: tangzh
 * @Date: 2019/5/16$ 7:23 PM$
 * 对于清理线程的初始化：1、静态代码块 2、@PostConstruct项目启动时
 **/
@Slf4j
public class LocalCache {

    private final static int DEFAULT_SIZE = 5000;
    private final static ConcurrentHashMap<String, Map<String,String>> CacheContext = new ConcurrentHashMap<>(DEFAULT_SIZE);
    private final static ScheduledExecutorService executor =
                         new LocalScheduleService(2,"localCache-schedule",true).getExecutor();
    private final static String VALUE = "VALUE";
    private final static String EXP_TM = "EXP_TM";
    private static final long DEF_EXP_TM = -1L;
    /**
     * 清理线程执行间隔时间
     */
    private final static long CLEAN_TM = 2 * 60 * 60;


    /**
     * 保存指定过期时间key、value
     * @param key
     * @param value
     * @param seconds
     */
    public static void setWithExp(String key,String value,long seconds){
        HashMap<String, String> data = new HashMap<>(2);
        data.put(VALUE,value);
        data.put(EXP_TM, HexUtil._10_to_64(seconds == DEF_EXP_TM ?
                                    DEF_EXP_TM : DateUtil.unixTime()+seconds));
        CacheContext.put(key,data);
    }

    /**
     * 保存无过期时间key、value
     * @param key
     * @param value
     */
    public static void set(String key,String value){
        setWithExp(key,value,DEF_EXP_TM);
    }

    /**
     * 获取本地Cache中value值
     * @param key
     * @return
     */
    public static String get(String key){
        if(exist(key)){
            Map<String,String> data = CacheContext.get(key);
            if(!isOutOfValidTime(data.get(EXP_TM))){
                return data.get(VALUE);
            }
            del(key);
        }
        return null;
    }

    /**
     * 从本地Cache中删除指定key
     * @param key
     */
    public static void del(String key){
        if(exist(key)){
            CacheContext.remove(key);
        }
    }

    /**
     * 时间是否过期
     */
    private static boolean isOutOfValidTime(String time){
        long expTm = HexUtil._64_to_10(time);
        return DEF_EXP_TM!=expTm && DateUtil.unixTime() > expTm;
    }
    private static boolean exist(String key){
        return CacheContext.get(key) != null;
    }

    /**
     * 清理线程-定时清除过期key
     */
    private static void startCleanThread(){
        executor.scheduleWithFixedDelay(() -> {
            for(Map.Entry<String,Map<String,String>> item : CacheContext.entrySet()){
                if(isOutOfValidTime(item.getValue().get(EXP_TM))){
                    del(item.getKey());
                }
            }
        },CLEAN_TM,CLEAN_TM, TimeUnit.SECONDS);
//        log.info("===============localCache clean thread started===============");
    }

    static {
        startCleanThread();
    }


}
