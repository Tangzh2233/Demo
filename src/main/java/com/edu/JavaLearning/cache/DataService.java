package com.edu.JavaLearning.cache;

import com.alibaba.fastjson.JSON;
import com.edu.dao.domain.User;
import com.edu.dao.mapper.ideaDemo.UserMapper;
import com.edu.util.RedisUtil;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

/**
 * @Author: tangzh Redis缓存工具类,主要是对网上缓存穿透,击穿,雪崩的解决实现
 * @Date: 2019/7/4$ 5:15 PM$
 **/
//@Component
public class DataService {

    /**
     * 缓存的空的对象的过期时间
     */
    private final static int EMPTY_TIME_OUT = 6 * 1000;
    /**
     * 正常对象的缓存时间
     */
    private final static int NORMAL_TIME_OUT = 5 * 60 * 1000;
    private final static int THRESHOL = 30 * 1000;

    private final static BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")), 101000, 0.0001);

    @Resource
    private UserMapper userMapper;

    @SuppressWarnings("unchecked")
    public String get(String key) {
        if (!bloomFilter.mightContain(key)) {
            return null;
        }
        String value = RedisUtil.get(key);
        if (StringUtils.isBlank(value)) {
            User user = userMapper.getUserByName(key);
            if (user == null) {
                RedisUtil.set(key, StringUtils.EMPTY, EMPTY_TIME_OUT);
            } else {
                value = JSON.toJSONString(user);
                RedisUtil.set(key, value, NORMAL_TIME_OUT + getRandom());
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
//    @PostConstruct
    public void initBloomFilter() {
        List<String> keys = userMapper.getAllKeys();
        for (String key : keys) {
            bloomFilter.put(key);
        }
    }

    public static int getRandom(){
        return new Random().nextInt(10000);
    }

}
