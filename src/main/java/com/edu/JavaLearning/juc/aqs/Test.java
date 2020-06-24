package com.edu.JavaLearning.juc.aqs;

import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/3/5 3:10 PM
 **/
public class Test {

    private static Jedis jedis = new Jedis();
    private Object lock = new Object();
    private volatile boolean flag = false;
    private volatile static int a = 202;


    public static void main(String[] args) throws InterruptedException {
        while (true){
            System.out.println(test());
            Thread.sleep(4000);
        }
    }

    public static String test(){
        return a + "hahahah";
    }

    public boolean get(String id){
        //guava的一个限流组件
        int num = Integer.valueOf(jedis.get(id));
        if(num <= 0){
            synchronized (lock){
                if(flag){
                    return false;
                }
                updateDb();
                flag = true;
            }
           return false;
        }
        //lock
        Long decrBy = jedis.decrBy(id, 1);
        if(decrBy <= 0){
            return false;
        }
        return true;
        //unlock
    }


    public void init(){
        Item item = selectDb();
        //lock
        if(StringUtils.isEmpty(jedis.get(item.id))){
            jedis.set(item.id,item.num);
        }
        //unlock
    }

    public Item selectDb(){
        String sql = "select num from db where id = xxx";
        return null;
    }

    public void updateDb(){
        String updateSql = "update db set num=0 where id=xxx";
    }

    static class Item{
       private String id;
       private String num;
    }

}
