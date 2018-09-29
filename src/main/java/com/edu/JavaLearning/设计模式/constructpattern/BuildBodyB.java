package com.edu.JavaLearning.设计模式.constructpattern;

/**
 * Created by Administrator on 2017/8/15.
 */
public class BuildBodyB implements Builder{
    @Override
    public String createArm() {
        return "create armB";
    }

    @Override
    public String createHead() {
        return "create headB";
    }

    @Override
    public String createLeg() {
        return "create legB";
    }
}
