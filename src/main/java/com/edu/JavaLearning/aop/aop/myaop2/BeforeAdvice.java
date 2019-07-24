package com.edu.JavaLearning.aop.aop.myaop2;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

/**
 * @author: tangzh
 * @date: 2019/7/23$ 11:04 AM$
 * @version: 1.0
 **/
public interface BeforeAdvice extends Advice {
    void before(Method method, Object[] args, Object target);
}
