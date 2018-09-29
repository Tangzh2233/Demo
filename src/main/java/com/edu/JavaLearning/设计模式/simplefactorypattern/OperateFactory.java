package com.edu.JavaLearning.设计模式.simplefactorypattern;

/**
 * Created by Administrator on 2017/8/9.
 */
public class OperateFactory {

    public static Operate createOperate(String opreate){
        Operate operate = null;
        switch (opreate){
            case "+":
                operate = new OperateAdd();
                break;
            case "/":
                operate = new OperateDiv();
                break;
            default:
                operate = null;
        }
        return operate;
    }
}
