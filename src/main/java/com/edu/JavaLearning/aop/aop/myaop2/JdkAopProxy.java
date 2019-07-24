package com.edu.JavaLearning.aop.aop.myaop2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author: tangzh
 * @date: 2019/7/23$ 10:56 AM$
 * @version: 1.0
 * jdk代理,实现aop
 * myAop1 -> myAop2 功能完善版
 *
 *
 **/
public class JdkAopProxy implements InvocationHandler{

    private Object targetClass;
    private List<Advice> advices;

    public JdkAopProxy() {
        this.advices = new LinkedList<>();
    }

    public Object getProxyInstance(){
        return Proxy.newProxyInstance(
                targetClass.getClass().getClassLoader(),
                targetClass.getClass().getInterfaces(),
                this);
    }

    public void setTargetClass(Object targetClass) {
        this.targetClass = targetClass;
    }

    public void setAdvice(Advice advice){
        Objects.requireNonNull(advice);
        advices.add(advice);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new Invocation(proxy, targetClass, method, args, targetClass.getClass(), advices);
        return invocation.process();
    }
}
