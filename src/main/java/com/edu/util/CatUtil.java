package com.edu.util;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

/**
 * @author Tangzhihao
 * @date 2018/4/27
 */

public class CatUtil {



    public static Transaction logTransaction(String name,String type){
        Transaction t = Cat.getProducer().newTransaction(name, type);
        return t;
    }

    public static void buildEvent(Transaction t,String type,String name,String s){
        String defType = type.substring(type.indexOf("|")+1);
        String defName = type.substring(0,type.indexOf("|"));
        Event event = Cat.newEvent(type, name);
        Event def = Cat.newEvent(defType, defName);
        event.setStatus(s);
        def.setStatus(s);
        t.addChild(def);
        t.addChild(event);
    }
    public static void logEvent(String type,String name){
        Cat.logEvent(type,name, Message.SUCCESS,null);
    }
    public static void logMetricCount(String metric){
        Cat.logMetricForCount(metric);
    }
    public static void logMetricDuration(String metric){
        Cat.logMetricForDuration(metric, 1L);
    }
    public static void logMetricSum(String name,double metric){
        Cat.logMetricForSum(name,metric);
    }

    public static void main(String[] args) {
        String type = "tang|用户登录";
        System.out.println(type.substring(0,type.indexOf("|")));
    }
}
