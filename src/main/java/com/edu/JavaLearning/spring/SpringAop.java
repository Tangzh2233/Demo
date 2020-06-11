package com.edu.JavaLearning.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/22 5:33 PM
 **/
@Order(1)
@Aspect
public class SpringAop {

    @Pointcut("@within(org.springframework.stereotype.Component)")
    public void pointcut(){

    }

    @Pointcut("execution(* com.edu.JavaLearning.spring.SpringAop.*(String))")
    public void pointcut2(){

    }

    @Around(value = "pointcut2()")
    public Object aop(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("before Aop");
        Object proceed = joinPoint.proceed();
        System.out.println("after Aop");
        return proceed;
    }

    private void print(String name){
        System.out.println(name);
    }

    private void print2(String name){
        System.out.println(name);
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/mybatis-tx.xml","/xml/springBean.xml");
        SpringAop bean = context.getBean(SpringAop.class);
        bean.print("print");
        bean.print2("print2");
    }
}
