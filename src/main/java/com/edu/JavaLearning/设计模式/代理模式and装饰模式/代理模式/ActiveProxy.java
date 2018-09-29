package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Tangzhihao
 * @date 2018/4/27
 * 动态代理:代理对象不需要实现接口。JDK的动态代理有一个限制,
 * 就是使用动态代理的对象必须实现一个或多个接口,如果想代理是没有实现接口的类,就可以使用Cglib实现.
 */

public class ActiveProxy {
    //维护一个代理对象
    private Object targetProxy;
    public ActiveProxy(Object target){
        this.targetProxy = target;
    }

    public Object getProxyInstance(){
        return Proxy.newProxyInstance(
                targetProxy.getClass().getClassLoader(),
                targetProxy.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("动态代理");
                        System.out.println(System.nanoTime());
                        Object object = method.invoke(targetProxy, args);
                        System.out.println(System.nanoTime());
                        return object;
                    }
                });
    }
}
