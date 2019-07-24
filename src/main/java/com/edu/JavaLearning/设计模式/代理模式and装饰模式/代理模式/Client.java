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
        Subject proxyInstace = (Subject)ActiveProxy.getProxyInstance(new RealSubject());
        proxyInstace.sendFlower();

        //CglibProxy
        CglibProxy cglibProxy = new CglibProxy(new CglibSubject());
        CglibSubject cglibInstance = (CglibSubject)cglibProxy.getTargetProxyInstance();
        cglibInstance.sendFlower();

        //Cglib
        CglibUniProxy uniProxy = new MyProxy();
        CglibSubject instance = (CglibSubject)uniProxy.getProxyInstance(CglibSubject.class);
        instance.sendFlower();
    }
}
