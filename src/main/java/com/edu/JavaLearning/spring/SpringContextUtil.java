package com.edu.JavaLearning.spring;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author: tangzh
 * @date: 2019/8/22$ 5:27 PM$
 * @version: 1.0
 *
 * 获取ApplicationContext的两种方式
 * 1.实现这个Spring接口自动注入（缺点：只能在指定类(实现接口)中获取）
 * @see org.springframework.context.ApplicationContextAware
 * 2.如下
 **/
public class SpringContextUtil implements ServletContextListener {

    private static ApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public static <T> T getBean(Class<T> tClass){
        return applicationContext.getBean(tClass);
    }

    public static Object getBena(String beanName){
        return applicationContext.getBean(beanName);
    }
}
