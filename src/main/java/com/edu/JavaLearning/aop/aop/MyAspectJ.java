package com.edu.JavaLearning.aop.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author Tangzhihao
 * @date 2018/5/3
 */
@Aspect
public class MyAspectJ {

    @Before("execution(* com.edu.service..*(..)) && @annotation(com.edu.JavaLearning.aop.aop.annotation.LogEvent)")
    public void before(){
        System.out.println("Method Before");
    }
}
