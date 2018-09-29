package com.edu.JavaLearning.设计模式.factorypattern;

/**
 * Created by Administrator on 2017/8/11.
 */
public class FactoryTest {
    public static void main(String[] args) {
        AddFactory addFactory = new AddFactory();
        Operate operate = addFactory.createOperate();
        operate.setNumber_a(12);
        operate.setNumber_b(24);
        System.out.println(operate.getResult());
    }
}
