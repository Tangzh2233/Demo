package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/8/11.
 */
public class Client implements InvocationHandler,Subject {


    private Subject clientProxy;

    /**
     * @see org.mybatis.spring.SqlSessionTemplate
     * 对数据库的操作连接操作本来是多例模式,一个事务一个sqlSession
     * 但是通过SqlSessionTemplate做代理。在SqlSessionTemplate的
     * @see org.mybatis.spring.SqlSessionTemplate.SqlSessionInterceptor
     * 来屏蔽这个多例。将多个sqlSession聚合在其中getSqlSession()中
     */
    public Client() {
        this.clientProxy = (Subject) java.lang.reflect.Proxy.newProxyInstance(
                Client.class.getClassLoader(),
                new Class[]{Subject.class},
                this);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        Client client = new Client();
        client.sendFlower();

        //StaticProxy
        Proxy proxy = new Proxy(new RealSubject());
        proxy.sendFlower();

        //ActiveProxy
        Subject proxyInstance = (Subject)ActiveProxy.getProxyInstance(new RealSubject());
        proxyInstance.sendFlower();

        //CglibProxy
        CglibProxy cglibProxy = new CglibProxy();
        CglibSubject cglibInstance = (CglibSubject)cglibProxy.getTargetProxyInstance(CglibSubject.class);
        cglibInstance.sendFlower();

        //Cglib
        CglibUniProxy uniProxy = new MyProxy();
        CglibSubject instance = (CglibSubject)uniProxy.getProxyInstance(CglibSubject.class);
        instance.sendFlower();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("clientProxy pre process");
        RealSubject realSubject = new RealSubject();
        return method.invoke(realSubject,args);
    }

    @Override
    public String sendFlower() {
        return clientProxy.sendFlower();
    }

    @Override
    public void sendDolls() {
        clientProxy.sendDolls();
    }

    @Override
    public void sendChocolate() {
        clientProxy.sendChocolate();
    }
}
