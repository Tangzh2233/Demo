package com.edu.JavaLearning.jdktest.spi;

/**
 * @Author: tangzh
 * @Date: 2019/4/28$ 2:24 PM$
 **/
public class JavaSpiImpl1 implements JavaSpi{
    @Override
    public void spiTestMethod() {
        System.out.println("Hello I`m Impl1");
    }
}
