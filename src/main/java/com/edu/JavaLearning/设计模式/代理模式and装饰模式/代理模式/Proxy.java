package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

/**
 * Created by Administrator on 2017/8/11.
 * 代理类，代理Subject的实际实现类RealSubject
 * 1.可以做到在不修改目标对象的功能前提下,对目标功能扩展.
 * 2.缺点:因为代理对象需要与目标对象实现一样的接口,
 * 所以会有很多代理类,类太多.同时,一旦接口增加方法,目标对象与代理对象都要维护.
 */
public class Proxy implements Subject {

    private RealSubject realSubject;
    public Proxy(RealSubject subject){
        this.realSubject = subject;
    }
    @Override
    public void sendFlower() {
        System.out.println("静态代理");
        realSubject.sendFlower();
    }

    @Override
    public void sendDolls() {
        realSubject.sendDolls();
    }

    @Override
    public void sendChocolate() {
        realSubject.sendChocolate();
    }
}
