package com.edu.JavaLearning.算法;

import com.alibaba.fastjson.JSON;
import com.edu.dao.domain.User;
import com.edu.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/15 1:47 PM
 * myBatis Redis实现二级缓存
 **/
public class RedisCache implements Cache {


    private String id;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RedisCache(String id) {
        System.out.println("Redis Cache init id: "+id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        String redisKey = String.valueOf(key.hashCode());
        RedisUtil.set(redisKey, JSON.toJSONString(value),60);
        System.out.println("Redis Cache putData key = " + redisKey + "value = " + JSON.toJSONString(value));
    }

    @Override
    public Object getObject(Object key) {
        String redisKey = String.valueOf(key.hashCode());
        String value = RedisUtil.get(redisKey);
        System.out.println("Redis Cache getData key = " + redisKey + "value" + value);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseArray(value, User.class);
        }
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        RedisUtil.del(String.valueOf(key.hashCode()));
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
