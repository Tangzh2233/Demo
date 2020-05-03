package com.edu.JavaLearning.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: tangzh
 * @date: 2019/8/22$ 5:27 PM$
 * @version: 1.0
 *
 * 获取ApplicationContext的两种方式
 * 1.实现这个Spring接口自动注入
 * @see org.springframework.context.ApplicationContextAware
 * 2.如下
 **/
public class SpringContextUtil implements ServletContextListener, ApplicationContextAware {

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

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }


    /**
     * 作用:运行期可以随时拿到request实例
     * @return
     */
    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 作用:运行期可以随时拿到response实例
     * @return
     */
    public static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }
}
