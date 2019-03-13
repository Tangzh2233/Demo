package com.edu.util.redisUtil;

import com.edu.common.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;

/**
 * @Author: tangzh
 * @Date: 2019/1/17$ 11:01 AM$
 * Redis 实现分布式锁
 * 照着网上写的，自我感觉问题很多!!!!
 **/
public class RedisLock {

    //作为锁的key有效时间,即2秒后某线程持有的锁自动释放！
    private long lockTimeOut = 2*1000;
    //若在1秒内没有获取到锁则认为此次锁获取失败！
    private long acquireTimeOut = 1000;
    private String lockKey = "Redis_Lock_";
    private static JedisPool pool;

    public RedisLock(String key){
        this.lockKey += key;
    }
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);
        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "127.0.0.1", 6379, 3000);
    }

    public String tryLock(){
        String id = "";
        Jedis jedis = pool.getResource();
        String lockValue = UUIDUtil.getUUID();
        long end = System.currentTimeMillis()+acquireTimeOut;
        try {
            //超出预定时间则认为此次获取锁失败
            //{while中会有很多没拿到锁重试[setnx()]的线程,直到超出设定时间若没有拿到锁则认为此线程获取锁失败}
            while (System.currentTimeMillis()<end){
                if(jedis.setnx(lockKey,lockValue)==1){
                    jedis.expire(lockKey,(int)lockTimeOut/1000);
                    id = lockValue;
                    return id;
                }
                if(jedis.ttl(lockKey)==-1){
                    jedis.expire(lockKey,(int)lockTimeOut/1000);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (JedisException e){

        } finally {
            jedis.close();
        }
        return id;
    }

    /**
      * @description:锁的拥有者或者锁到期才能释放锁
    **/
    public boolean unLock(String id){
        boolean unLockFlag = false;
        if(StringUtils.isBlank(id)){
            return unLockFlag;
        }
        Jedis jedis = pool.getResource();
        try {
            while (true){
                jedis.watch(lockKey);
                if(id.equals(jedis.get(lockKey))){
                    Transaction transaction = jedis.multi();
                    transaction.del(lockKey);
                    List<Object> result = transaction.exec();
                    if(result==null){
                        continue;
                    }
                    unLockFlag = true;
                }
                jedis.unwatch();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return unLockFlag;
    }
}
/**
  * @description测试：500线程争夺5件商品
**/
class Test{

    public static void main(String[] args) {
        ProductManager service = new ProductManager();
        for(int i=0;i<500;i++){
            new Job(service).start();
        }
    }

}

class ProductManager{
    //商品数量
    private int num = 5;
    private RedisLock lock = new RedisLock("User");

    public void doing(){
        String id = "";
        try {
            id = lock.tryLock();
            if(num<=0){
                System.out.println("商品售罄");
                return;
            }
            //判断是否拿到锁
            if(StringUtils.isNotBlank(id)){
                num--;
                System.out.println(Thread.currentThread().getName()+"秒杀成功"+num);
            }else {
                System.out.println(Thread.currentThread().getName()+"秒杀失败");
            }
        } finally {
            lock.unLock(id);
        }
    }
}
class Job extends Thread{
    private ProductManager manager;

    Job(ProductManager manager){
        this.manager = manager;
    }
    @Override
    public void run() {
        manager.doing();
    }
}
