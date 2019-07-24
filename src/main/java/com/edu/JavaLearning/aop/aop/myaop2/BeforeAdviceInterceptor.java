package com.edu.JavaLearning.aop.aop.myaop2;

import java.util.Objects;

/**
 * @author: tangzh
 * @date: 2019/7/23$ 11:14 AM$
 * @version: 1.0
 **/
public class BeforeAdviceInterceptor implements MethodInterceptor{

    private final BeforeAdvice beforeAdvice;

    public BeforeAdviceInterceptor(BeforeAdvice beforeAdvice) {
        Objects.requireNonNull(beforeAdvice);
        this.beforeAdvice = beforeAdvice;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        this.beforeAdvice.before(invocation.getMethod(),invocation.getArguments(),invocation.getTarget());
        return invocation.process();
    }
}
