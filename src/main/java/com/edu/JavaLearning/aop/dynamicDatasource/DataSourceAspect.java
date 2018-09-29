package com.edu.JavaLearning.aop.dynamicDatasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author Tangzhihao
 * @date 2018/1/31
 */

public class DataSourceAspect {

    public void intercept(JoinPoint point) throws NoSuchMethodException {

        //获得代理对象类(类注解)
        Class<?> target = point.getTarget().getClass();
        //获取代理方法(方法注解)
        MethodSignature signature = (MethodSignature) point.getSignature();

        reloveAspect(target,signature.getMethod());
    }

    /**
     * @author: Tangzhihao
     * @date: 2018/1/31 15:32
     * @description:设置当前线程的datasource的key
     */
    public void reloveAspect(Class<?> clazz, Method method) throws NoSuchMethodException {

        try {
            //获取方法的参数数组
            Class<?>[] types = method.getParameterTypes();

            if(clazz.isAnnotationPresent(DataSourceAnnotation.class)){
                DataSourceAnnotation annotation = clazz.getAnnotation(DataSourceAnnotation.class);
                DataSourceContextHolder.setDataSourceType(annotation.value());
            }
            //mentod annotation replace type annotation
            Method m = clazz.getMethod(method.getName(), types);
            if(m!=null && m.isAnnotationPresent(DataSourceAnnotation.class)){
                DataSourceAnnotation annotation = m.getAnnotation(DataSourceAnnotation.class);
                DataSourceContextHolder.setDataSourceType(annotation.value());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }
}
