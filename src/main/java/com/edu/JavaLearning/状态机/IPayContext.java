package com.edu.JavaLearning.状态机;


/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 上午10:34$
 **/
public interface IPayContext {
    void doNext();
    void doLast();
    void setState(IPayState state);
    int getNextService();
}
