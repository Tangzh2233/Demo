package com.edu.JavaLearning.设计模式.适配器.类对象适配器;

/**
 * @author Tangzhihao
 * @date 2018/3/5
 */

public class Src300V implements TargetV{

    private int src = 300;
    @Override
    public int outputV() {
        System.out.println("我是"+src+"V");
        return src;
    }
}
