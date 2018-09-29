package com.edu.JavaLearning.设计模式.factorypattern;

/**
 * Created by Administrator on 2017/8/11.
 */
public class MulFactory implements Factory{
    @Override
    public Operate createOperate() {
        return new OperateMul();
    }
}
