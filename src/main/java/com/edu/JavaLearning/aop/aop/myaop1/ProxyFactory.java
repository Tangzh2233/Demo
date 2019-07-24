package com.edu.JavaLearning.aop.aop.myaop1;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author : tangzh
 * @date : 2019/7/22$ 8:04 PM$
 * @version 1.0 无法实现Around环绕增强
 * 简单的代理工厂实现
 * ================================
 * 查看springAop,jdk代理的部分源码,设计的非常巧妙. 进行myAop2的功能完善
 **/
public class ProxyFactory {

    private Object target;
    private List<Advices> beforeAdvices;
    private List<Advices> afterAdvices;

    public ProxyFactory() {
        //LinkedList保证顺序性
        this.beforeAdvices = new LinkedList<>();
        this.afterAdvices = new LinkedList<>();
    }

    public Object getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                for (Advices advice : beforeAdvices) {
                    BeforeAdvice beforeAdvice = (BeforeAdvice) advice;
                    beforeAdvice.before(o, method, objects, methodProxy);
                }
                Object invokeSuper = methodProxy.invokeSuper(o, objects);
                for(Advices advice : afterAdvices){
                    AfterAdvice afterAdvice = (AfterAdvice)advice;
                    afterAdvice.after(o, method, objects, methodProxy);
                }
                return invokeSuper;
            }
        });
        return enhancer.create();
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setAdvice(Advices advice) {
        Objects.requireNonNull(advice);
        if (advice instanceof BeforeAdvice) {
            beforeAdvices.add(advice);
        }
        if (advice instanceof AfterAdvice) {
            afterAdvices.add(advice);
        }
    }
}
