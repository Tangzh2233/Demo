package com.edu.JavaLearning.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/8/6 15:50
 * @description
 **/
@Import({MyImport.class,MyImport1.class,MyImport2.class})
public class SpringImport {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringImport.class);
        for(String name : context.getBeanDefinitionNames()){
            System.out.println(name);
        };
    }
}


class MyImport{
}
class MyImport1{
}

class MyImport2 implements ImportSelector{

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"com.edu.JavaLearning.spring.MyImport3"};
    }
}

class MyImport3 implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(MyImport3.class);
        registry.registerBeanDefinition("Lala",beanDefinition);
    }
}

