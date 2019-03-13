package com.edu.util.zookeeper;

import java.lang.annotation.*;

/**
 * @Author: tangzh
 * @Date: 2019/1/21$ 8:00 PM$
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DRM {
    String value() default "";
}
