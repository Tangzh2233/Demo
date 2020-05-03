package com.edu.JavaLearning.设计模式.代理模式and装饰模式.装饰模式;

/**
 * Created by Administrator on 2017/8/9.
 */
public class TShirts extends Decorator {
    @Override
    public void show() {
        super.show();
        System.out.println("T恤");
    }
}
