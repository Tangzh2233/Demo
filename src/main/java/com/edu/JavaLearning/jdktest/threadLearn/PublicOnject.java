package com.edu.JavaLearning.jdktest.threadLearn;

/**
 * @author Tangzhihao
 * @date 2018/1/3
 */

public class PublicOnject {

    synchronized public void strPrint(){
        System.out.println(Thread.currentThread().getName()+"进入公共资源方法");
        if("a".equals(Thread.currentThread().getName())){
            System.out.println("a线程已被suspend");
           // Thread.currentThread().suspend();
        }

    }
}
