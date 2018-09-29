package com.edu.JavaLearning.aop.dynamicDatasource;

/**
 * @author Tangzhihao
 * @date 2018/1/31
 */

public class DataSourceContextHolder {

    private final static ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSourceType(String str){
        CONTEXT_HOLDER.set(str);
    }

    public static String getDataSourceType(){
        return CONTEXT_HOLDER.get();
    }

    public static void removeDataSourceType(){
        CONTEXT_HOLDER.remove();
    }
}
