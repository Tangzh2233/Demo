package com.edu.JavaLearning.设计模式.simplefactorypattern;

/**
 * Created by Administrator on 2017/8/9.
 */
public class FactoryTest {
    public static void main(String[] args) {
        Operate operate;
        operate = OperateFactory.createOperate("/");
        operate.setNumber_A(2);
        operate.setNumber_B(0);
        System.out.println(operate.getResult());
    }
}
