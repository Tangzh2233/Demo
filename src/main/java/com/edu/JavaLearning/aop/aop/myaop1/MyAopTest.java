package com.edu.JavaLearning.aop.aop.myaop1;

import com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式.CglibSubject;
import java.lang.reflect.Method;

/**
 * @Author: tangzh
 * @Date: 2019/7/22$ 8:32 PM$
 **/
public class MyAopTest {
    public static void main(String[] args) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(new CglibSubject());
        proxyFactory.setAdvice(new MyBefoAdvice());
        proxyFactory.setAdvice(new MyAfterAdvice());
        proxyFactory.setAdvice(new MyAfterAdvice());

        CglibSubject proxyInstance = (CglibSubject)proxyFactory.getProxyInstance();
        proxyInstance.sendFlower();
    }
}

class MyBefoAdvice implements BeforeAdvice{

    @Override
    public void before(Object var1, Method var2, Object[] var3, Object var4) {
        System.out.println("前置通知");
    }
}

class MyAfterAdvice implements AfterAdvice{

    @Override
    public void after(Object var1, Method var2, Object[] var3, Object var4) {
        System.out.println("后置通知");
    }
}
