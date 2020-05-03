package com.edu.JavaLearning.设计模式.状态模式;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/8 7:10 PM
 **/
public abstract class State {
    public abstract void doNext(int result);
    public abstract int getService();
}
