package com.edu.JavaLearning.算法;

import com.edu.util.RedisUtil;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/11 11:50 AM
 * 高并发控制。限流领劵问题。Redis + RateLimiter
 **/
public class HighConcurrencyControl {

    private DistributedConsistency distributedConsistency;

    public DistributedConsistency getDistributedConsistency() {
        return distributedConsistency;
    }

    /**
     * @see Introspector#getBeanInfo()
     * @see Introspector#getTargetPropertyInfo()
     * springBean.xml
     * SpringBean 依赖注入分为byName和byType两种方式。
     * 但是Spring是如何知道一个Bean中哪些属性需要注入。就是备注中getTargetPropertyInfo()
     * 通过分割public get set方法。来确定类中有哪些属性
     *
     * @see org.mybatis.spring.mapper.MapperFactoryBean
     * 此类只是在
     * @see org.mybatis.spring.mapper.ClassPathMapperScanner
     * 中设置了 GenericBeanDefinition的setAutowireMode为byType
     * 最终在Spring会依据以下两个方法
     * @see org.mybatis.spring.mapper.MapperFactoryBean#setSqlSessionFactory(SqlSessionFactory)
     * @see org.mybatis.spring.mapper.MapperFactoryBean#setSqlSessionTemplate(SqlSessionTemplate)
     * 认为 MapperFactoryBean 存在属性 SqlSessionFactory、SqlSessionTemplate 然后在容器中通过
     * byType 的方式找到相应的Bean执行这两个方法。最终达到MapperFactoryBean的sqlSession属性注入。
     *
     * 当前方法也是一样。在xml中设置了autowire。最终Spring在初始化HighConcurrencyControl时会调用
     * 当前方法,进行属性distributedConsistency的赋值
     * @param distributedConsistency
     */
    public void setDistributedConsistency(DistributedConsistency distributedConsistency) {
        this.distributedConsistency = distributedConsistency;
    }

    private static RateLimiter rateLimiter = RateLimiter.create(20);

    //已更新标识
    private volatile boolean flag = true;


    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/springBean.xml", "/xml/mybatis-tx.xml");
        HighConcurrencyControl instance = (HighConcurrencyControl) context.getBean("highConcurrencyControl");

//        ClassPathXmlApplicationContext context2 = new ClassPathXmlApplicationContext("/xml/springBean.xml", "/xml/mybatis-tx.xml");
//        HighConcurrencyControl instance2 = (HighConcurrencyControl) context2.getBean("highConcurrencyControl");
//
//        Random random = new Random();
//
//        ExecutorService executorService = Executors.newFixedThreadPool(10);

        instance.testQuery("1001");
        instance.testQuery("1001");
        instance.testQuery("1001");
        instance.testUpdate("1001",111);
        instance.testQuery("1001");
        instance.testQuery("1001");
        instance.testQuery("1001");


//        for (int i = 0; i < 100; i++) {
//            new Thread(new Task(instance, "1001")).start();
//        }
//        for (int i = 0; i < 100; i++) {
//            int nextInt = random.nextInt(100);
//            if(nextInt < 50){
//                executorService.submit(new queryTask(instance));
//            }else {
//                executorService.submit(new updateTask(instance2,nextInt));
//            }
//        }
//        executorService.shutdown();
    }


    public void testQuery(String id) {
        String value = distributedConsistency.queryNumByUserNo(id);
        System.out.println(Thread.currentThread().getName() + "获取" + id + "值: " + value);
    }

    public void testUpdate(String id, int value) {
        distributedConsistency.updateByUserNo(id, value);
        System.out.println(Thread.currentThread().getName() + "更新" + id + "值: " + value);
    }

    public boolean acquire(String id) {
        //限流。1s请求未成功置为失败
        if (rateLimiter.tryAcquire(2000, TimeUnit.MILLISECONDS)) {
            String num = RedisUtil.get(id);
            if (StringUtils.isNotBlank(num) && Integer.valueOf(num) > 0) {
                if (flag) {
                    if (Integer.valueOf(RedisUtil.get(id)) > 0) {
                        if (RedisLock.tryLock(id, 2)) {
                            try {
                                Long decrBy = RedisUtil.decrBy(id);
                                if (decrBy >= 0) {
                                    return true;
                                }
                            } finally {
                                RedisLock.unLock(id);
                            }
                        }
                    }
                }
            } else {
                if (Integer.valueOf(num) <= 0 && flag) {
                    synchronized (this) {
                        if (flag) {
                            distributedConsistency.updateByUserNo(id, 0);
                            flag = false;
                        }
                    }
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + "被限制访问");
        return false;
    }


    public void init() {
        final String initKey = "UserCacheInit";
        final String initFlag = "UserCacheFlag";
        boolean lock = RedisLock.tryLock(initKey, 3);
        try {
            if (lock) {
                if (StringUtils.isNotBlank(RedisUtil.get(initFlag))) {
                    System.out.println(Thread.currentThread().getName() + "数据已初始化....return");
                    return;
                }
                System.out.println(Thread.currentThread().getName() + "正在初始化数据");
                distributedConsistency.init();
                RedisUtil.set(initFlag, "true");
            }
        } finally {
            RedisLock.unLock(initKey);
        }
    }

    static class Task implements Runnable {

        private HighConcurrencyControl instance;
        private String productId;

        public Task(HighConcurrencyControl instance, String productId) {
            this.instance = instance;
            this.productId = productId;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "抢购" + productId + "开始.......");
            if (instance.acquire(productId)) {
                System.out.println(Thread.currentThread().getName() + "抢购 " + productId + "成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "抢购 " + productId + "失败");
            }
//            instance.init();
        }
    }

    static class queryTask implements Runnable{

        HighConcurrencyControl instance;

        public queryTask(HighConcurrencyControl instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            instance.testQuery("1001");
        }
    }

    static class updateTask implements Runnable{

        HighConcurrencyControl instance;
        int value;

        public updateTask(HighConcurrencyControl instance, int value) {
            this.instance = instance;
            this.value = value;
        }

        @Override
        public void run() {
            instance.testUpdate("1001",value);
        }
    }
}
