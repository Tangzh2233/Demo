package com.edu.JavaLearning.aop.aop.myaop2;

/**
 * @author: tangzh
 * @date: 2019/7/23$ 11:43 AM$
 * @version: 1.0
 **/
public class AfterAdviceInterceptor implements MethodInterceptor{

    private final AfterAdvice afterAdvice;

    public AfterAdviceInterceptor(AfterAdvice afterAdvice) {
        this.afterAdvice = afterAdvice;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        Object reVal = invocation.process();
        this.afterAdvice.after(reVal,invocation.getMethod(),invocation.getArguments(),invocation.getTarget());
        return reVal;
    }
}
