package com.edu.JavaLearning.aop.aop.annotation;

import java.lang.annotation.*;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/12/16 5:18 PM
 * 自定义注解
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {
    String value();
}
