package com.edu.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/9/20 20:38
 * @description 权限标识
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionLimit {

    boolean limit() default true;

    boolean admin() default false;
}
