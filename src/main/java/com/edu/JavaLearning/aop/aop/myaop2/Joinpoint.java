package com.edu.JavaLearning.aop.aop.myaop2;

/**
 * @author: tangzh
 * @date: 2019/7/23$ 11:39 AM$
 * @version: 1.0
 **/
public interface Joinpoint {
    Object process() throws Throwable;
}
