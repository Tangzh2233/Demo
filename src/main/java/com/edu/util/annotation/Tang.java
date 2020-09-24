package com.edu.util.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/7/20 13:55
 * @description 自定义注解
 **/
@Target({TYPE,METHOD,FIELD}) //注解适用位置
@Inherited                   //子类可继承
@Documented                  //该注解将被包含在javadoc中
@Retention(RetentionPolicy.RUNTIME) //定义该注解的生命周期,SOURCE:在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。@Override, @SuppressWarnings都属于这类注解。
                                    //CLASS:在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式,运行期无法获得
                                    //RUNTIME:始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。我们自定义的注解通常使用这种方式。
public @interface Tang {
    String[] values() default "";

    String message() default "param check not allow";
}
