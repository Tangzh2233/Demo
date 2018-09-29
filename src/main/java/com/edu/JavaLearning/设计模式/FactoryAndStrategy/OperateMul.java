package com.edu.JavaLearning.设计模式.FactoryAndStrategy;

/**
 * Created by Administrator on 2017/8/9.
 */
public class OperateMul extends Operate{
    public double getResult(){
        double result = 0;
        result = getNumber_a()*getNumber_b();
        return result;
    }
}
