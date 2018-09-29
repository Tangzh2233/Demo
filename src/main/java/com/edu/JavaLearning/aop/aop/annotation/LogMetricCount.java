package com.edu.JavaLearning.aop.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linzhihao
 * @date 2018/5/3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMetricCount {
    /**
     * 属性名称
     * @return
     */
    //String[] name();

    /**
     * 需要发送的业务指标
     * @return
     */
    String[] metricKey();
}
