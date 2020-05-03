package com.edu.JavaLearning.算法;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/7 5:23 PM
 * Redis 分布式锁
 * redisLock 对相同的key设置值
 **/
@Component
public class RedisLock {

    private final static Logger log = LoggerFactory.getLogger(RedisLock.class);

    public static JedisPool POOL;
    private static String PRE_KEY = "Redis_Lock_";

    @Value("ip")
    private static String ip;

    @Value("port")
    private static int port;
    //锁获取等待时间
    private static int acquireTime = 1000;

    private static int product = 5;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数
        config.setMaxTotal(20);
        //最大空闲数
        config.setMaxIdle(10);
        config.setMaxWaitMillis(5000);
        POOL = new JedisPool(config, "127.0.0.1", 6379, 5000);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Task()).start();
        }
    }

    /**
     * value 为当前线程ID
     *
     * @param key
     * @param second
     * @return
     */
    public static boolean tryLock(String key, int second) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        String lockKey = getKey(key);
        Jedis jedis = null;
        //重试时间acquireTime
        try {
            jedis = POOL.getResource();
            long end = System.currentTimeMillis() + acquireTime;
            while (System.currentTimeMillis() < end) {
                String value = String.valueOf(Thread.currentThread().getId());
                if ("OK".equals(jedis.set(lockKey, value, "NX", "EX", second))) {
                    return true;
                } else {
                    if (jedis.ttl(lockKey) == -1) {
                        jedis.expire(lockKey, second);
                    }
                    Thread.sleep(100);
                }

            }
        } catch (Exception e) {
            log.error("Redis Lock Acquire Fail");
        } finally {
            close(jedis);
        }
        return false;
    }

    public static void unLock(String key) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        Jedis jedis = null;
        String lockKey = getKey(key);
        try {
            jedis = POOL.getResource();
            String value = String.valueOf(Thread.currentThread().getId());
            String lockValue = jedis.get(lockKey);
            if (StringUtils.isBlank(lockValue) || !value.equals(lockValue)) {
                return;
            }
            jedis.del(lockKey);
        } catch (Exception e) {
            log.error("Redis Lock Release Fail");
        } finally {
            close(jedis);
        }
    }

    private static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    private static String getKey(String key) {
        return PRE_KEY + key;
    }


    static class Task implements Runnable {

        @Override
        public void run() {
            try {
                if (tryLock("101", 1)) {
                    if (product > 0) {
                        --product;
                        System.out.println(Thread.currentThread().getName() + "获取成功：" + product);
                    } else {
                        System.out.println(Thread.currentThread().getName() + "获取失败");
                    }
                }
            } catch (Exception e) {

            } finally {
                unLock("101");
            }
        }
    }
}
