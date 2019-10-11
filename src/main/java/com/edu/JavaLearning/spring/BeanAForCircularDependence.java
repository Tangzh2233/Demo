package com.edu.JavaLearning.spring;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/10/10 8:54 PM
 * SpringBean循环依赖问题
 **/
@Component
public class BeanAForCircularDependence {

    @Resource
    private BeanBForCircularDependence beanBForCircularDependence;

    public void printA(){
        beanBForCircularDependence.printB();
    }

    public void print(){
        System.out.println("我是BeanA");
    }
}
