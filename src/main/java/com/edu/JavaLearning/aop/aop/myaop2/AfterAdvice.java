package com.edu.JavaLearning.aop.aop.myaop2;

import java.lang.reflect.Method;

/**
 * @author: tangzh
 * @date: 2019/7/23$ 11:05 AM$
 * @version: 1.0
 **/
public interface AfterAdvice extends Advice{
    void after(Object returnValue, Method method, Object[] args, Object target);
}
