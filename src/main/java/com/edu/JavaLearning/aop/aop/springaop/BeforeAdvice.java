package com.edu.JavaLearning.aop.aop.springaop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author Tangzhihao
 * @date 2018/5/4
 * aop前置增强
 */

public class BeforeAdvice implements MethodBeforeAdvice{
    @Override
    public void before(Method method, Object[] args, Object obj) throws Throwable {
        String name = (String) args[0];
        System.out.println("前置增强"+name);
    }
}
