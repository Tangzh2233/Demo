package com.edu.util;

import com.edu.common.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import javax.swing.*;
import java.util.List;

/**
 * @author Tangzhihao
 * @date 2018/2/28
 */

public class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static final int MAXIDLE = Integer.valueOf(OpenConfig.getConfig("redis.maxIdle"));
    private static final boolean TESTONBORROW = Boolean.valueOf(OpenConfig.getConfig("redis.testOnBorrow"));
    private static final long SMEIT = Long.valueOf(OpenConfig.getConfig("redis.softMinEvictableIdleTime"));
    private static final int MAXTOTAL = Integer.valueOf(OpenConfig.getConfig("redis.maxTotal"));
    private static final long MAXWATIMILLS = Long.valueOf(OpenConfig.getConfig("redis.maxWaitMillis"));
    private static final String HOST = OpenConfig.getConfig("redis.host");
    private static final int PORT = Integer.valueOf(OpenConfig.getConfig("redis.port"));

    private static JedisPool pool;

    static {
        try {
            JedisPoolConfig config = getJedisPoolConfig();
            pool = new JedisPool(config,"127.0.0.1",6379);
        }
        catch (Exception e){
            logger.error("Redis 初始化失败",e);
        }

    }
    public static JedisPool getPool() {
        return pool;
    }

    private static JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxIdle(MAXIDLE);
        config.setTestOnBorrow(TESTONBORROW);
        config.setSoftMinEvictableIdleTimeMillis(SMEIT);
        config.setMaxTotal(MAXTOTAL);
        config.setMaxWaitMillis(MAXWATIMILLS);

        return config;
    }

    public static String ping(){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ping();
        } catch (Exception e) {
            return null;
        }finally {
            close(jedis);
        }
    }

    public static void set(String key,String value){
        Assert.isEmpty(key,"key is null");
        Assert.isEmpty(value,"value is null");

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        }finally {
            close(jedis);
        }
    }

    public static void set(String key,String value,int expire){
        Assert.isEmpty(key,"key is null");
        Assert.isEmpty(value,"value is null");

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, expire,value);
        }finally {
            close(jedis);
        }
    }

    public static String get(String key){
        Assert.isEmpty(key,"key is null");
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("Redis Exception",e);
        } finally {
            close(jedis);
        }
        return null;
    }

    public static void expire(String key,int s){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.expire(key,s);
        } catch (Exception e) {
            logger.error("Redis Exception",e);
        } finally {
            close(jedis);
        }
    }

    public static void del(String... keys){
        for(String key : keys){
            Assert.isEmpty(key,"key is null");
        }
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(keys);
        } catch (Exception e) {
            logger.error("Redis Exception",e);
            throw new IllegalStateException();
        } finally {
            close(jedis);
        }
    }

    public static long setnx(String key,String value){
        Assert.isEmpty(key,"key must is not null");
        Assert.isEmpty(value,"value must is not null");
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setnx(key, value);
        }finally {
            close(jedis);
        }
    }

    public static long getRedisTime(){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return Long.parseLong(jedis.time().get(0));
        }finally {
            close(jedis);
        }
    }

    public static String getSet(String key,String value){
        Assert.isEmpty(key,"key must is not null");
        Assert.isEmpty(value,"value must is not null");
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getSet(key, value);
        }finally {
            close(jedis);
        }
    }

    public static long ttl(String key){
        Assert.isEmpty(key,"key must is not null");
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ttl(key);
        }finally {
            close(jedis);
        }
    }

    public static void close(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

    public static Boolean exist(String key){
        Assert.isEmpty(key,"key must is not null");
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        }finally {
            close(jedis);
        }
    }

    public static Long decrBy(String key){
        Assert.isEmpty(key,"key must is not null");
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decrBy(key,1);
        }finally {
            close(jedis);
        }
    }

    public static long hset(String key,String field,String value){
        Assert.isEmpty(key,"key must is not null");
        Assert.isEmpty(field,"key must is not null");
        Assert.isEmpty(value,"key must is not null");
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hset(key,field,value);
        }finally {
            close(jedis);
        }
    }

    public static List<String> lrange(String key, int from) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, from, jedis.llen(key));
        } finally {
            close(jedis);
        }
    }

    public static Long publish(String channel,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.publish(channel, value);
        } finally {
            close(jedis);
        }
    }

    public static void subscribe(String channel, JedisPubSub jedisPubSub) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(jedisPubSub, channel);
        } finally {
            close(jedis);
        }
    }


}
