package com.edu.JavaLearning.springdataredis;

import redis.clients.jedis.Jedis;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Administrator on 2017/8/21.
 */
public class RedisTest2 {
    public void updateToken(Jedis jedis,String user,String token,String item){
        long timestamp = System.currentTimeMillis() / 1000;
        jedis.hset("impl:",token,user);//token 更新
        jedis.zadd("recent:",timestamp,token);//记录某一token最后一次出现的时间
        //item商品的标识
        if(item != null){
            jedis.zadd("viewed:"+token,timestamp,item);//记录某token的浏览记录
            jedis.zremrangeByRank("viewed:"+token,0,-26);
        }
    }
    public boolean canCache(Jedis jedis,String request){
        try {
            URL url = new URL(request);
            HashMap<String, String> params = new HashMap<>();
            if(url.getQuery() != null){

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public class CleanSessonThread extends Thread{
        private Jedis jedis;
        private int limit;
        private boolean quit;

        public CleanSessonThread(int limit){
            this.jedis = RedisUtil.getJedis();
            this.limit = limit;
        }
        public void quit(){quit = false;}

        @Override
        public void run(){
            while(!quit){
                long size = jedis.zcard("recent:");
                if (size < limit){
                    try {
                        //如果token数量未超过限制，线程休眠一秒
                        sleep(1000);
                    } catch (InterruptedException e) {
                        //中断当前线程
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                long endIndex = Math.min(size - limit,100);
                Set<String> tokenSet = jedis.zrange("recent:", 0, endIndex - 1);
                String[] tokens = tokenSet.toArray(new String[tokenSet.size()]);
                jedis.del(tokenSet.toArray(new String[tokenSet.size()]));
                jedis.hdel("impl:",tokens);
                jedis.zrem("recent:",tokens);
            }
        }
    }
}

