package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

import org.springframework.cglib.proxy.*;

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
        sendGift();
    }

    public void sendGift(){
        System.out.println("送礼物");
    }
}

class CglibProxy implements MethodInterceptor{
    private Object targetProxy;

    public Object getTargetProxyInstance(Class clazz) throws IllegalAccessException, InstantiationException {
        //工具类
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);//需要创建子类的类
        targetProxy = clazz.newInstance();
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

/**
 * CGLIB解析
 * wiki:https://www.cnblogs.com/loveer/p/11588126.html
 */
abstract class CglibUniProxy implements
        MethodInterceptor,
        InvocationHandler,
        NoOp,
        LazyLoader,
        Dispatcher,
        Cglib {

    public Object getProxyInstance(Class clazz){
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        en.setCallback(this);
        return en.create();
    }

    /**
     * Method.invoke() 直接原方法调用
     * @see CglibSubject#sendFlower() 只会输出1个"Cglib代理" 内部sendGift()直接走本地方法。
     * MethodProxy.invokeSuper() 代理方法调用
     * @see CglibSubject#sendFlower() 会输出2个"Cglib代理" 内部sendGift()走sendGift()代理方法,又会输出一个"Cglib代理"
     *
     * @param o
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     * @see MethodInterceptor
     * 类似around-advice他和InvocationHandler区别是,参数中对了MethodProxy,可以拿到代理方法
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("Cglib代理");
        return methodProxy.invokeSuper(o, objects);
    }

    /**
     * @param o
     * @param method
     * @param objects
     * @return
     * @throws Throwable
     * @see InvocationHandler
     * 它的使用方式和MethodInterceptor差不多。
     * 需要注意的一点是，所有对invoke()方法的参数proxy对象的方法调用都会被委托给同一个InvocationHandler，
     * 所以可能会导致无限循环。
     */
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return null;
    }

    /**
     * @return
     * @throws Exception
     * @see LazyLoader
     * @see Dispatcher
     */
    @Override
    public Object loadObject() throws Exception {
        return null;
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
