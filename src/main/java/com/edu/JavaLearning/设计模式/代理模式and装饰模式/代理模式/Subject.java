package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

/**
 * Created by Administrator on 2017/8/11.
 */
public interface Subject {
    void sendFlower();
    void sendDolls();
    void sendChocolate();
}

class RealSubject implements Subject{

    @Override
    public void sendFlower() {
        System.out.println("送花");
    }

    @Override
    public void sendDolls() {
        System.out.println("送娃娃");
    }

    @Override
    public void sendChocolate() {
        System.out.println("送巧克力");
    }
}