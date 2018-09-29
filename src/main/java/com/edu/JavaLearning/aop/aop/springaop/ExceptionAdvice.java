package com.edu.JavaLearning.aop.aop.springaop;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * @author Tangzhihao
 * @date 2018/5/4
 */

public class ExceptionAdvice implements ThrowsAdvice{

    public void afterThrowing(Method method,Object[] args,Object obj,Exception e){
        System.out.println("-------------");
        System.out.println("method:"+method.getName());
        System.out.println("异常:"+e.getMessage());
        System.out.println("事务回滚！");
    }
}
