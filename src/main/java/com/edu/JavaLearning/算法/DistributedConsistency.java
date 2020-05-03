package com.edu.JavaLearning.算法;

import com.edu.dao.domain.User;
import com.edu.dao.mapper.ideaDemo.UserMapper;
import com.edu.util.RedisUtil;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/12 11:38 AM
 * 分布式一致性代码实现
 * 此处的补偿可以换成发送MQ的方式自产自销
 **/
@Component
public class DistributedConsistency {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Resource
    private UserMapper userMapper;


    /**
     * 数据更新成功但是redis删除失败情况
     * 需要补偿更新
     */
    private static ArrayBlockingQueue<String> needCompensationQueue = new ArrayBlockingQueue<>(1000);


    public String queryNumByUserNo(String userNo) {
        String value = RedisUtil.get(userNo);
        if (StringUtils.isBlank(value)) {
            User user = userMapper.queryUserByUserNo(userNo);
            if (user != null) {
                value = String.valueOf(user.getNum());
                RedisUtil.set(user.getUserNo(), value);
            }
        }
        return value;
    }

    public void init(){
        List<User> all = userMapper.getAll();
        for(User user : all){
            RedisUtil.set(user.getUserNo(),String.valueOf(user.getNum()));
        }
    }

    /**
     * 先更新再删除
     * @param userNo
     * @param value
     */
    public synchronized void updateByUserNo(String userNo, int value) {

        RedisUtil.del(userNo);

        userMapper.updateByUserNo(userNo, value);
        try {
            RedisUtil.del(userNo);
        } catch (Exception e) {
            try {
                needCompensationQueue.put(userNo);
                //有异常发生时再唤醒补偿线程
                new Thread(new CompensationTask()).start();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 补偿Task
     */
    static class CompensationTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    String item = needCompensationQueue.take();
                    RedisUtil.del(item);
                }
            } catch (InterruptedException e) {

            }
        }
    }

    @Data
    @Builder
    public static class OCS<T> {
        private long version;
        private T value;
    }
}
