package com.edu.JavaLearning.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/9/17 5:04 PM
 **/
@Component
public class SpringBean implements InitializingBean, DisposableBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private SpringEventPublishListener publishListener;

    private String beanId;
    private String beanName;
    private String data;

    public SpringBean() {
        System.out.println("无参构造方法执行ing");
    }
    public SpringBean(String beanId, String beanName, String data) {
        this.beanId = beanId;
        this.beanName = beanName;
        this.data = data;
    }


    public void init(){
        System.out.println("init-method 执行ing");
    }

    @Override
    public void destroy(){
        System.out.println("destroy-method 执行ing");
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void print(){
        //依赖测试
        publishListener.print();
        System.out.println("类实例成功,print方法执行");
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/springBean.xml");
        SpringBean bean = (SpringBean)context.getBean("springBean");
        bean.print();
        //实例化自定义Event
        UserDefinedEvent event = new UserDefinedEvent("hello", "UserDefinedEventPublish");
        //事件发布,注意Listener是否监听到并做出逻辑输出
        context.publishEvent(event);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean afterPropertiesSet 初始化");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
