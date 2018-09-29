package com.edu.JavaLearning.aop.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解要求方法有返回值，且返回值是一个Json字符串或者一个JavaBean对象
 * 通常我们使用这个注解在方法上，表示方法执行了某一个事件
 * 该注解针对的粒度：一个执行了某个事件的方法。一个Event与一个Method，一一对应。
 * 举个例子:我们假设login这个方法是用来登录的，在一个完整的过程中，调用了method1、method2、method3等方法最终完成了login的逻辑
 * 我们假设login这个方法只完成了登录这个事件，我们就能够使用该注解表示这个方法执行了登录事件
 * 而如果我们想要监控更加精确的过程如就必须要在method1、method2、method3方法上面也添加该注解
 *
 * 注意:通常如果一个方法的逻辑代码涉及多个事件的发生，我们就需要把它抽离成一个方法，然后标注该注解。
 *
 * @author linzhihao
 * @date 2018/5/3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogEvent {
    /**
     * 通常我们指定事件类型，可以为该方法执行的某个事件
     * @return
     */
    String type();

    /**
     * 在该系统被认为正确的响应码
     * @return
     */
    String[] expectedCodes();

    /**
     * 存放响应码的JavaBean属性
     * @return
     */
    String codeProperty();

    /**
     * 存放返回信息的JavaBean属性
     * @return
     */
    String messageProperty();
}
