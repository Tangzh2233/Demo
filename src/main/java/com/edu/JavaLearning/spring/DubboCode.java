package com.edu.JavaLearning.spring;

import com.edu.Api.DubboProviderApi;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;

/**
 * @Author: tangzh
 * @Date: 2019/4/30$ 2:44 PM$
 * 测试Spring集成dubbo的启动流程
 * 相关总结及面试题
 * https://www.jianshu.com/p/5047918f1289
 * https://youzhixueyuan.com/dubbo-interview-question-answers.html
 **/
public class DubboCode {
//    @Resource
//    private DubboProviderApi dubboProviderApi;

    public static void main(String[] args) {
        DubboCode client = new DubboCode();
        client.invoke();
    }

    public void invoke(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/dubbo.xml");
        DubboProviderApi bean = (DubboProviderApi)context.getBean("dubboProviderApi");
        bean.myProvider();
//        dubboProviderApi.myProvider();
    }
}
