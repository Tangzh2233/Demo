package com.edu.JavaLearning.aop.aop.springaop;

import org.springframework.aop.framework.ProxyFactory;

import java.sql.SQLException;

/**
 * @author Tangzhihao
 * @date 2018/5/4
 */

public class SpringAopTest {

    public static void main(String[] args) throws SQLException {
        ProxyFactory proxyFactory = new ProxyFactory();
        //前置增强
        BeforeAdvice beforeAdvicea = new BeforeAdvice();
        //后置增强
        AfterAdvice afterAdvice = new AfterAdvice();
        //环绕增强
        AroundAdvice aroundAdvice = new AroundAdvice();
        //异常增强
        ExceptionAdvice exceptionAdvice = new ExceptionAdvice();
        NativeWaiter target = new NativeWaiter();

        proxyFactory.setInterfaces(target.getClass().getInterfaces());//指定对接口进行代理[jdk动态代理]
        proxyFactory.setOptimize(true);//启用优化[又将使用Cglib代理]
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(beforeAdvicea);
        proxyFactory.addAdvice(afterAdvice);
        proxyFactory.addAdvice(aroundAdvice);
        proxyFactory.addAdvice(exceptionAdvice);


        Waiter aj = (Waiter)proxyFactory.getProxy();//创建目标实例
        aj.serverto("tang");
        aj.throwException();
    }
}
