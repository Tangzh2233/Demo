package com.edu.JavaLearning.jdktest.UtilsTest;

import org.springframework.context.annotation.Bean;

/**
 * @author Tangzhihao
 * @date 2017/12/15
 */

public class InitTest {


    public void init(){
        System.out.println("this is init");
    }

    public InitTest(){
        init();
        System.out.println("this is construct");
    }

    static {
        System.out.println("this is static");
    }

    public void method(){
        System.out.println("this is method");
    }

}
class test{
    @Bean(initMethod = "init")
    public static void main(String[] args) {
        new InitTest().method();
    }
}