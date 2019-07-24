package com.edu.JavaLearning.aop.aop.myaop2;

/**
 * @author: tangzh
 * @date: 2019/7/23$ 11:11 AM$
 * @version: 1.0
 **/
public interface MethodInterceptor extends Advice {
    Object invoke(Invocation invocation) throws Throwable;
}
