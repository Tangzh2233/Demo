package com.edu.JavaLearning.springdataredis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Created by Administrator on 2017/8/17.
 */
public class RedisUtil {

    private static Jedis jedis=null;
    //public static final Logger log = (Logger) LoggerFactory.getLogger(RedisUtil.class);
    public static void main(String[] args) throws JedisConnectionException {
        try {
            jedis = new JedisPool("127.0.0.1", 6379).getResource();
       }catch (Exception e){
            System.out.println(e.toString());
        }
        if(jedis!=null){
            System.out.println("redis connect success");
        }else{
            String yu = jedis.get("yu");
            System.out.println("redis connect fail---"+yu);
        }
    }
    //获取redis连接
    public static Jedis getJedis(){
        if(jedis==null){
            try {
                jedis = new Jedis("127.0.0.1", 6379);
            } catch (Exception e) {
//            log.warning("redis连接获取异常");
            }
            return jedis;
        }
        return jedis;
    }
    //断开redis连接
    public static void closeJedis(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }
}
