package com.edu.JavaLearning.aop.aop;

import com.edu.JavaLearning.aop.aop.annotation.CheckLogin;
import com.edu.JavaLearning.aop.aop.springaop.AnnotationFinder;
import com.edu.JavaLearning.spring.SpringContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tangzhihao
 * @date 2018/5/3
 */
@Aspect
public class MyAspectJ {

    @Around("execution(* com.edu.controller.*.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint){

        HttpServletRequest request = SpringContextUtil.getRequest();
        CheckLogin checkLogin = AnnotationFinder.find(joinPoint, request, CheckLogin.class);
        if(checkLogin != null){
            //todo check login
        }
        return null;
    }


}
