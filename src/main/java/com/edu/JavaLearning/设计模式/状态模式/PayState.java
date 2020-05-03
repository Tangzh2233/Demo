package com.edu.JavaLearning.设计模式.状态模式;

import java.util.Random;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/8 7:18 PM
 **/
public abstract class PayState extends State{

    protected Random random = new Random();

    protected Context context;
    public PayState(){}
    public PayState(Context context){this.context = context;}
}
