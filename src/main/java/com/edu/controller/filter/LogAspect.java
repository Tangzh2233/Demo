package com.edu.controller.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: tangzh
 * @Date: 2018/10/30$ 下午3:20$
 * 数据toJSONString()+PropertyPreFilter的方式。不能直接使用JSON.toJSONString().
 * 否则或报错"It is illegal to call this method if the current request is not in asynchronous mode"
 **/
@Component
@Aspect
public class LogAspect {

    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private LogJsonFilter jsonFilter;

    public LogAspect(){
        String[] nName = { "inputStream", "response", "request", "req", "resp" };
        this.jsonFilter = new LogJsonFilter(nName);
    }

    @Around("execution(* com.edu.controller.login.*.*(..))")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Class<?> target = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        String fullName = target.getName()+"."+methodName;
        Object[] args = point.getArgs();
        logger.info("请求方法:{} 请求参数:{}",fullName, toJSONString(args));
        Object result;
        try {
            result = point.proceed(args);
        } catch (Throwable throwable) {
            logger.error("请求方法:{} 异常 请求参数{}",fullName,toJSONString(args),throwable.getMessage());
            throw throwable;
        }
        logger.info("返回方法:{} 返回结果:{}",fullName, toJSONString(result));
        return result;
    }

    private String toJSONString(Object args) {
        return JSON.toJSONString(args, this.jsonFilter, SerializerFeature.WriteMapNullValue);
    }
}
