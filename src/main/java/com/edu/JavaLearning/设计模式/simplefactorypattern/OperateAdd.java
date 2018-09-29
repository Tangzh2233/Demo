package com.edu.JavaLearning.设计模式.simplefactorypattern;

/**
 * Created by Administrator on 2017/8/9.
 */
public class OperateAdd extends Operate{
    public double getResult(){
        double result = 0;
        result = getNumber_A()+getNumber_B();
        return  result;
    }
}
