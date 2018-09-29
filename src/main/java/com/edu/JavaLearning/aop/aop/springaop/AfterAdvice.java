package com.edu.JavaLearning.aop.aop.springaop;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author Tangzhihao
 * @date 2018/5/4
 * aop后置增强
 */

public class AfterAdvice implements AfterReturningAdvice{
    @Override
    public void afterReturning(Object result, Method method, Object[] args, Object obj) throws Throwable {
        System.out.println("后置增强");
    }
}
