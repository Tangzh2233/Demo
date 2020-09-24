package com.edu.JavaLearning.spring.spring中的设计模式;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/8/12 17:45
 * @description spring中的单例模式
 **/
public class SpringSingleTon {

    protected Integer num = 2;
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/bean.xml");
        SpringSingleTon bean = (SpringSingleTon)context.getBean("springSingleTon1");
        SpringSingleTon bean1 = (SpringSingleTon)context.getBean("springSingleTon1");
        System.out.println(bean);
        System.out.println(bean1);
    }
}


