package com.edu.util;

import com.dianping.cat.Cat;
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

    public static void buildEvent(Transaction t,String type,String name){

    }
    public static void logEvent(String type,String name){
        Cat.logEvent(type,name, Message.SUCCESS,null);
    }
    public static void logMetricCount(String metric){
        Cat.logMetricForCount(metric);
    }
    public static void logMetricDuration(String metric){
        Cat.logMetricForDuration(metric,1l);
    }
    public static void logMetricSum(String name,double metric){
        Cat.logMetricForSum(name,metric);
    }
}
