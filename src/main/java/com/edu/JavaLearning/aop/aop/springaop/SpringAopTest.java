package com.edu.JavaLearning.aop.aop.springaop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.*;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author Tangzhihao
 * @date 2018/5/4
 * SpringAop的底层实现为Jdk动态代理和Cglib代理。具体二者的区别与简单使用参考:
 * @see com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式.ActiveProxy
 * @see com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式.CglibSubject
 *
 * SpringAop的具体实现方式:
 *
 * @version Jdk代理
 * ==============代理类的创建==============
 * @see ProxyFactory :代理工厂创建代理实例,及设置某些属性
 * @see ProxyFactory#addAdvice(Advice) 添加代理类的增强服务,如前/后置通知,环绕通知,异常处理等
 * @see ProxyFactory#setOptimize(boolean) 此处设置的属性,可以使有接口的targetClass(默认为jdk代理)使用Cglib代理
 * @see ProxyFactory#createAopProxy()
 * @see org.springframework.aop.framework.DefaultAopProxyFactory#createAopProxy(AdvisedSupport)
 * @see JdkDynamicAopProxy#getProxy()     Jdk代理类的创建     Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
 * @see ObjenesisCglibAopProxy#getProxy() Cglib代理类的创建   Enhancer enhancer = new Enhancer();
 * =============Jdk代理类方法的执行==============
 * Jdk代理的实际执行是在InvocationHandler接口中的invoke方法,JdkDynamicAopProxy 实现了InvocationHandler接口
 * @see java.lang.reflect.InvocationHandler#invoke(Object, Method, Object[])
 * @see org.springframework.aop.framework.JdkDynamicAopProxy#invoke(Object, Method, Object[])
 * 获取ProxyFactory中设置的Advice服务列表,最终进行属性包装,执行逻辑交给了ReflectiveMethodInvocation
 * @see ReflectiveMethodInvocation#proceed()
 * 此处的advice执行采用了递归的执行方式,递归的结束条件为advice全部执行完。内部维护了一个currentInterceptorIndex判断执行到哪一个advice
 * 当advice执行完成以后,代理方法自己的逻辑执行在
 * @see ReflectiveMethodInvocation#invokeJoinpoint() 其实就是
 * @see Method#invoke(Object, Object...)
 *
 * =============Cglib代理类的方法执行============
 * 创建Cglib代理实例并设置CallBack.此处的CallBack为 CglibAopProxy的诸多实现类
 * @see ObjenesisCglibAopProxy#getProxy(ClassLoader)
 * @see ObjenesisCglibAopProxy#createProxyClassAndInstance(Enhancer, Callback[])
 * 方法的执行实际为。
 * @see CglibAopProxy 中包装了很多 MethodInterceptor的实现类,
 * @see org.springframework.cglib.proxy.MethodInterceptor#intercept(Object, Method, Object[], MethodProxy)
 * eg:
 * @see CglibAopProxy.DynamicAdvisedInterceptor#intercept(Object, Method, Object[], MethodProxy)
 * 同jdk代理的方法执行类似,Cglib代理的方法执行最终包装交给了
 * @see CglibAopProxy.CglibMethodInvocation#proceed() ReflectiveMethodInvocation的子类重写了invokeJoinpoint
 * @see CglibAopProxy.CglibMethodInvocation#invokeJoinpoint()。区别执行了public方法和非public方法
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

//        ProxyTest target = new ProxyTest();
        proxyFactory.setInterfaces(target.getClass().getInterfaces());//指定对接口进行代理[jdk`TAMTMSFCT` (`TAM_BAT_NO`,`MERC_ID`,`TAM_BUS_TYP`,`TAM_FIL_DT`,`FIL_DT_SEQ`,`TAM_FIL_NM`,`FILE_RCV_DT`,`TAM_BEG_TM`,`TAM_END_TM`,`RMK`,`AC_DT`,`TOT_UP_AMT`,`TOT_UP_CNT`,`PASS_TOT_CNT`,`PASS_TOT_AMT`,`ROL_TOT_AMT`,`ROL_TOT_NUM`,`SUC_TOT_AMT`,`SUC_TOT_NUM`,`FAIL_TOT_AMTF`,`FAIL_TOT_AMTS`,`OTHER_INF`,`TAM_FIL_STS`,`TAM_SRV`,`TAM_TX_CD`,`TM_SMP`,`IF_URGENCY`,`TAM_FIL_TM`,`ARM_NO`,`TOT_PASS_FEEAMT`,`TXN_ACC`,`FAIL_TOT_CNT`,`REJ_TOT_CNT`,`CMT_TOT_CNT`,`SEND_TOT_CNT`,`REJ_TOT_AMT`,`SEND_TOT_AMT`,`CMT_TOT_AMT` ,`MERC_TYP`,`MERC_TRD_CLS`,`MERC_NAM`,`HLD_NO`,`RTN_FLG`,`BACK_FILE`,`SYS_CNL`,`BUS_CNL`,`TX_TYP`,`BUS_TYP`,`PS_APPR_STS`,`PS_APPR_OPR`,`PS_APPR_BATNO`,`PS_APPR_DT`,`PS_APPR_TM`,`PS_APPR_RMK`,`PS_WFF_STS`,`PS_WFF_OPR`,`PS_WFF_DT`,`PS_WFF_TM`,`PS_WFF_RMK`,`PS_WFF_BATNO`,`TAM_WF_STS`)]
//        proxyFactory.setOptimize(true);//启用优化[又将使用Cglib代理]
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(beforeAdvicea);
        proxyFactory.addAdvice(afterAdvice);
        proxyFactory.addAdvice(aroundAdvice);
        proxyFactory.addAdvice(exceptionAdvice);


        Waiter aj = (Waiter)proxyFactory.getProxy();//创建目标实例
//        ProxyTest aj = (ProxyTest)proxyFactory.getProxy();
        aj.serverto("tang");
//        aj.throwException();
//        aj.test("tang");
    }
}

class ProxyTest{
    public void test(String name){
        System.out.println("代理测试方法");
    }
}

class test{

}
