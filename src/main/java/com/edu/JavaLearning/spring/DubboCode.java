package com.edu.JavaLearning.spring;

import com.edu.Api.DubboProviderApi;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;

/**
 * @Author: tangzh
 * @Date: 2019/4/30$ 2:44 PM$
 **/
public class DubboCode {
    @Resource
    private DubboProviderApi dubboProviderApi;

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/dubbo.xml");
        DubboProviderApi bean = (DubboProviderApi)context.getBean("dubboProviderImpl");
        bean.myProvider();
    }
}
