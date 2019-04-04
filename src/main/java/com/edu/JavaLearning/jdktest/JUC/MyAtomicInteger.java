package com.edu.JavaLearning.jdktest.JUC;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tangzh
 * @Date: 2019/3/15$ 8:08 PM$
 **/
public class MyAtomicInteger {
    private static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        int i = atomicInteger.incrementAndGet();
        int i1 = atomicInteger.updateAndGet(operand -> operand+10);
        System.out.println(i+" value:"+atomicInteger.get()+" i1:"+i1);
    }
}
