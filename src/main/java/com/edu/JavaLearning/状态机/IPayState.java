package com.edu.JavaLearning.状态机;

/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 下午7:14$
 **/
public interface IPayState {
    int getNextService();
    void doNext();
}
