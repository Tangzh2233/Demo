package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Tangzhihao
 * @date 2018/4/27
 * 上面的静态代理和动态代理模式都是要求目标对象是实现一个接口的目标对象,
 * 但是有时候目标对象只是一个单独的对象,并没有实现任何的接口,
 * 这个时候就可以使用以目标对象子类的方式类实现代理,这种方法就叫做:Cglib代理.
 * Cglib代理,也叫作子类代理,它是在内存中构建一个子类对象从而实现对目标对象功能的扩展.
 * JDK的动态代理有一个限制,就是使用动态代理的对象必须实现一个或多个接口,
 * 如果想代理没有实现接口的类,就可以使用Cglib实现.
 */

public class CglibSubject {
    public void sendFlower(){
        System.out.println("送花");
    }
}

class CglibProxy implements MethodInterceptor{
    private Object targetProxy;
    public CglibProxy(Object object){
        this.targetProxy = object;
    }

    public Object getTargetProxyInstance(){
        //工具类
        Enhancer en = new Enhancer();
        en.setSuperclass(targetProxy.getClass());//需要创建子类的类
        en.setCallback(this);
        return en.create();//通过字节码技术动态创建子类实例
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("Cglib代理");
        return method.invoke(targetProxy,objects);
    }
}

interface Cglib{
    void before();
    void after();
}

abstract class CglibUniProxy implements MethodInterceptor,Cglib{

    public Object getProxyInstance(Class clazz){
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        en.setCallback(this);
        return en.create();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        before();
        Object invokeSuper = methodProxy.invokeSuper(o, objects);
        after();
        return invokeSuper;
    }

}

class MyProxy extends CglibUniProxy{

    @Override
    public void before() {
        System.out.println("前置通知");
    }

    @Override
    public void after() {
        System.out.println("后置通知");
    }
}
