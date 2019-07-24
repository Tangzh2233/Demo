package com.edu.JavaLearning.aop.aop.myaop1;

import java.lang.reflect.Method;

/**
 * @Author: tangzh
 * @Date: 2019/7/22$ 8:19 PM$
 **/
public interface BeforeAdvice extends Advices{
    void before(Object var1, Method var2, Object[] var3, Object var4);
}
