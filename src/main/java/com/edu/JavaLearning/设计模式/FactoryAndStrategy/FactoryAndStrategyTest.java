package com.edu.JavaLearning.设计模式.FactoryAndStrategy;

/**
 * Created by Administrator on 2017/8/9.
 */
public class FactoryAndStrategyTest {
    public static void main(String[] args) {
        OperateContext oc = new OperateContext("*");
        System.out.println(oc.getResult(1,2));
    }
}
