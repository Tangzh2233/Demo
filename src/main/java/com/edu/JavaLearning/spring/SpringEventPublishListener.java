package com.edu.JavaLearning.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: tangzh
 * @Date: 2019/1/22$ 10:47 AM$
 * 自定义事件监听逻辑
 * Spring内置事件描述: https://blog.csdn.net/liyantianmin/article/details/81017960
 **/
public class SpringEventPublishListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof UserDefinedEvent){
            System.out.println((((UserDefinedEvent) event).getMessage()));
        }
    }
}


class SpringEventPublishTest{
    public static void main(String[] args) {
        //向Context中注入Listener的Bean
        ApplicationContext context = new ClassPathXmlApplicationContext("/xml/beanConfig.xml");
        //实例化自定义Event
        UserDefinedEvent event = new UserDefinedEvent("hello", "UserDefinedEventPublish");
        //事件发布,注意Listener是否监听到并做出逻辑输出
        context.publishEvent(event);
    }
}

/**
  * @description: 自定义事件
**/
class UserDefinedEvent extends ApplicationEvent{

    private String message;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public UserDefinedEvent(Object source,String msg) {
        super(source);
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

