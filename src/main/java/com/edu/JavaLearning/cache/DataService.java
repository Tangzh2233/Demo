package com.edu.JavaLearning.cache;

import com.alibaba.fastjson.JSON;
import com.edu.dao.domain.User;
import com.edu.dao.mapper.ideaDemo.UserMapper;
import com.edu.util.RedisUtil;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author: tangzh Redis缓存工具类,主要是对网上缓存穿透,击穿,雪崩的解决实现
 * RateLimiter令牌桶使用初探
 * 当令牌不足时,不会阻塞本次调用,而是有下一次调用买单,具体展示参考main中的demo
 * 实现原理:实际上RateLimiter是通过一次调用来决定下一次可以调用的时间点。
 * 若下一次调用的时间点小于记录时间则阻塞等待。
 * 若下一次调用的时间点大雨记录时间点则立即返回,然后根据quire值和某些参数计算出下一次的可调用时间点
 * <p>
 * eg: permitsPerSecond = 1  8:00创建
 * acquire(1) 此时时间 8:005 > 8:00 直接返回                setLastTime = 8:01
 * acquire(1) 此时时间 8:007 < 8:01 计算时间差,阻塞至8:01    setLastTime = 8:02
 * acquire(5) 此时时间 8:03 > 8:02 直接返回                 setLastTime = 8:02+5/permitsPerSecond = 8:07
 * acquire(10)此时时间 8:04 < 8:07 计算时间差,03阻塞至8:07    setLastTime = 8:07+10=8:17
 * acquire(1) 此时时间 8:10 < 8:17 计算时间差,阻塞至8:17      setLastTime = 8:18
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

    /**
     * 从开始8:00 -> 8:17 用时约17个单位时间。
     * console的waitTime与之对应
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println("startTime: " + System.nanoTime());
            RateLimiter rateLimiter = RateLimiter.create(1);
            // 8:00
            long nanoTime = System.nanoTime();
            System.out.println("startTime: " + nanoTime);
            //current > 8:00 直接返回 setLastTime = 8:01
            System.out.println("current: " + System.nanoTime() + "waitTime: " + rateLimiter.acquire(20));
            //current < 8:01 阻塞至 8:01 setLastTime = 8:02
            System.out.println("current: " + System.nanoTime() + "waitTime: " + rateLimiter.acquire(20));
            //休眠,保证currentTime > lastTime
            Thread.sleep(5000);
            //此时currentTime > lastTime 直接返回 setLastTime = 8:07
            System.out.println("current: " + System.nanoTime() + "waitTime: " + rateLimiter.acquire(20));
            //此时currentTime < lastTime 阻塞至8:07 setLastTime = 8:17
            System.out.println("current: " + System.nanoTime() + "waitTime: " + rateLimiter.acquire(10));
            //此时currentTime < lastTime 阻塞至8:17
            System.out.println("current: " + System.nanoTime() + "waitTime: " + rateLimiter.acquire(1));
            long endTime = System.nanoTime() - nanoTime;
            System.out.println("耗时:" + endTime);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException){
                System.out.println("主键或唯一键冲突异常");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public String get(String key) {
        if (!bloomFilter.mightContain(key)) {
            return null;
        }
        String value = RedisUtil.get(key);
        if (StringUtils.isBlank(value)) {
            User user = userMapper.getUserByName(key);
            if (user == null) {
                //缓存空值,设置超时时间
                RedisUtil.set(key, StringUtils.EMPTY, EMPTY_TIME_OUT);
            } else {
                value = JSON.toJSONString(user);
                //缓存实际数据,设置不同过期时间,防止缓存同时失效造成缓存雪崩
                RedisUtil.set(key, value, NORMAL_TIME_OUT + getRandom());
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
//    @PostConstruct
    public void initBloomFilter() {
//        List<String> keys = userMapper.getAllKeys();
//        for (String key : keys) {
//            bloomFilter.put(key);
//        }
    }

    public static int getRandom() {
        return new Random().nextInt(10000);
    }

}
