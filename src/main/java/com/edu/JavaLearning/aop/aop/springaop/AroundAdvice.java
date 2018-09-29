package com.edu.JavaLearning.aop.aop.springaop;

import com.alibaba.fastjson.JSON;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author Tangzhihao
 * @date 2018/5/4
 * aop环绕增强
 */

public class AroundAdvice implements MethodInterceptor{
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Object[] args = methodInvocation.getArguments();//目标方法入参
        String name = (String) args[0];
        System.out.println("环绕前置"+method + JSON.toJSONString(args));
        Object result = methodInvocation.proceed();//调用目标方法
        System.out.println("环绕后置"+method + JSON.toJSONString(result));
        return result;
    }
}
