package com.edu.JavaLearning.设计模式.simplefactorypattern;

/**
 * Created by Administrator on 2017/8/9.
 */
public class OperateDiv extends Operate{
    public double getResult(){
        double result = 0;
        if(getNumber_B()==0){
            System.out.println("非法除数");
            return 0;
        }else{
            result = getNumber_A()/getNumber_B();
        }
        return result;
    }
}
