package com.edu.JavaLearning.设计模式.constructpattern;

/**
 * Created by Administrator on 2017/8/15.
 */
public class BuildBodyA implements Builder {
    @Override
    public String createArm() {
        return "create armA";
    }

    @Override
    public String createHead() {
        return "create headA";
    }

    @Override
    public String createLeg() {
        return "create legA";
    }
}
