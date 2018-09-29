package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

/**
 * Created by Administrator on 2017/8/11.
 */
public class Client {

    public static void main(String[] args) {
        //StaticProxy
        Proxy proxy = new Proxy(new RealSubject());
        proxy.sendFlower();

        //ActiveProxy
        ActiveProxy activeProxy = new ActiveProxy(new RealSubject());
        Subject proxyInstace = (Subject)activeProxy.getProxyInstance();
        proxyInstace.sendDolls();

        //CglibProxy
        CglibProxy cglibProxy = new CglibProxy(new CglibSubject());
        CglibSubject cglibInstance = (CglibSubject)cglibProxy.getTargetProxyInstance();
        cglibInstance.sendFlower();

        //Cglib
        CglibUniProxy uniProxy = new CglibUniProxy();
        CglibSubject instance = (CglibSubject)uniProxy.getProxyInstance(CglibSubject.class);
        instance.sendFlower();
    }
}
