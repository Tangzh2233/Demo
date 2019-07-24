package com.edu.JavaLearning.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tangzh
 * @Date: 2019/4/16$ 8:15 PM$
 * UnSafe:java代码直接访问内存 https://www.cnblogs.com/throwable/p/9139947.html
 * volatile:修饰变量不可被线程缓存,每次数据均从主存读取。
 *
 *
 **/
public class Stu_Atomic{

    private AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {

    }

}
