package com.edu.util.redisUtil.impl;

import com.edu.util.redisUtil.JedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @author Tangzhihao
 * @date 2018/5/23
 */

public class JedisClientSingleImpl implements JedisClient{

    @Resource
    private JedisPool jedisPool;

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    @Override
    public Long hset(String hkey, String key, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(hkey, key, value);
        jedis.close();
        return result;
    }

    @Override
    public String hget(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(hkey, key);
        jedis.close();
        return result;
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long count = jedis.incr(key);
        jedis.close();
        return count;
    }

    @Override
    public Long expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        Long count = jedis.expire(key, seconds);
        jedis.close();
        return count;

    }

    @Override
    public Long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long count = jedis.ttl(key);
        jedis.close();
        return count;
    }

    @Override
    public Long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long count = jedis.del(key);
        jedis.close();
        return count;
    }

    @Override
    public Long hdel(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        Long count = jedis.hdel(hkey, key);
        jedis.close();
        return count;
    }
}
