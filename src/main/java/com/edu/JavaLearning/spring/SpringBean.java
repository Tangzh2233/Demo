package com.edu.JavaLearning.spring;

import com.edu.JavaLearning.算法.HighConcurrencyControl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/9/17 5:04 PM
 **/
@Service("springBean")
public class SpringBean implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private static ApplicationContext applicationContext;

    @Resource
    private SpringEventPublishListener publishListener;

    //@Qualifier 当用byType注解发现有多个相同类型的实现时,Qualifier来表示具体使用哪个
    @Autowired
    @Qualifier("highConcurrencyControl")
    private HighConcurrencyControl highConcurrencyControl;


    //@Required
    //Required注解表示,SpringBean在初始化之前必须执行这个方法对属性首先赋值
    //否则Bean会创建失败,错误信息：Property 'publishListener' is required for bean 'springBean'
    public void setPublishListener(SpringEventPublishListener publishListener) {
        this.publishListener = publishListener;
    }

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
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/springBean.xml","xml/mybatis-tx.xml");
        SpringBean bean = context.getBean(SpringBean.class);
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
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
