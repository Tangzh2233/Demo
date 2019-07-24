package com.edu.JavaLearning.aop.aop.myaop2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: tangzh
 * @date: 2019/7/23$ 11:25 AM$
 * @version: 1.0
 **/
public class Invocation implements Joinpoint{

    protected final Object proxy;

    protected final Object target;

    protected final Method method;

    protected Object[] arguments;

    protected final Class<?> targetClass;

    private final List<Advice> adviceList;

    private int currentAdviceIndex = -1;

    public Invocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass,List<Advice> advices) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.adviceList = advices;
    }

    public Object getProxy() {
        return proxy;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public List<Advice> getAdviceList() {
        return adviceList;
    }

    @Override
    public Object process() throws Throwable {
        if(currentAdviceIndex == adviceList.size()-1){
            return invokeJoinpoint();
        }
        Advice currentAdvice = adviceList.get(++currentAdviceIndex);
        return  ((MethodInterceptor) currentAdvice).invoke(this);
    }

    private Object invokeJoinpoint() throws Throwable {
        try {
            return method.invoke(target, arguments);
        } catch (IllegalAccessException e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
