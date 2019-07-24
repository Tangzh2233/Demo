package com.edu.JavaLearning.aop.aop.myaop1;

import java.lang.reflect.Method;

/**
 * @Author: tangzh
 * @Date: 2019/7/22$ 8:20 PM$
 **/
public interface AfterAdvice extends Advices{
    void after(Object var1, Method var2, Object[] var3, Object var4);
}
