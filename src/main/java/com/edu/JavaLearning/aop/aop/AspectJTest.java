package com.edu.JavaLearning.aop.aop;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author Tangzhihao
 * @date 2018/5/3
 */

public class AspectJTest {
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    private FileSystemXmlApplicationContext xmlBeanFactory;

    public static void main(String[] args) {

    }

//    @LogEvent(type = "ILoginService",name = "login",value = {"tang","yu","liu"})
    public void MethodA(){
        System.out.println("MethodA");
    }
}
