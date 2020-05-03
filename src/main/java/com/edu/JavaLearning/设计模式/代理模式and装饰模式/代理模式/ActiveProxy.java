package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

import java.lang.reflect.Proxy;

/**
 * @author Tangzhihao
 * @date 2018/4/27
 * 动态代理:代理对象不需要实现接口。JDK的动态代理有一个限制,
 * 就是使用动态代理的对象必须实现一个或多个接口,如果想代理是没有实现接口的类,就可以使用Cglib实现.
 *
 * 实际实现类等效于,构建一个代理类,实现要代理对象的接口中的所有方法。实际执行体为invoke()方法,
 * 实现的接口方法只做一个传值处理,值分别是 1.代理类本身 2.代理的方法本身 3.方法所需参数
 */

public class ActiveProxy {

    public static Object getProxyInstance(Object targetProxy) {
        return Proxy.newProxyInstance(
                targetProxy.getClass().getClassLoader(),
                targetProxy.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    System.out.println("动态代理");
                    System.out.println(System.nanoTime());
                    Object object = method.invoke(targetProxy, args);
                    System.out.println(System.nanoTime());
                    return object;
                });
    }

}
