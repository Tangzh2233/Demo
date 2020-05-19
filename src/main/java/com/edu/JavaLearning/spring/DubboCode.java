package com.edu.JavaLearning.spring;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.dubbo.DubboRegistryFactory;
import com.edu.Api.DubboProviderApi;
import com.google.common.collect.Lists;
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

    /**
     * 代码dubbo调用
     */
    public void dubboInvoke(){
        ApplicationConfig config = new ApplicationConfig();
        config.setName("app");

        ReferenceConfig<DubboProviderApi> rf = new ReferenceConfig<>();
        rf.setInterface(DubboProviderApi.class);
        rf.setUrl("dubbo://127.0.0.1:20880/com.edu.Api.DubboProviderApi");
        rf.setApplication(config);
        rf.setVersion("1.0.0");

        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setAsync(false);
        methodConfig.setName("myProvider");

        rf.setMethods(Lists.newArrayList(methodConfig));
        rf.get().myProvider();
    }

    public void invoke(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/dubbo.xml");
        DubboProviderApi bean = (DubboProviderApi)context.getBean("dubboProviderApi");
        bean.myProvider();

        dubboInvoke();
    }
}
