package com.edu.JavaLearning.spring;

import com.edu.dao.domain.User;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/10/17 8:08 PM
 **/
@Component
public class FactoryBeanOrBeanFactory implements FactoryBean<User> {

    @Override
    public User getObject() throws Exception {
        return new User();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/springBean.xml");
        Object bean = context.getBean("factoryBeanOrBeanFactory");
        Object bean1 = context.getBean("&factoryBeanOrBeanFactory");
        System.out.println(bean.getClass());
        System.out.println(bean1.getClass());
    }
}
