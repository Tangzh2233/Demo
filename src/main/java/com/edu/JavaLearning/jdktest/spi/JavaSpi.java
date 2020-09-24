package com.edu.JavaLearning.jdktest.spi;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.extension.SPI;

import java.util.ServiceLoader;

/**
 * @Author: tangzh
 * @Date: 2019/4/26$ 7:44 PM$
 * @SPI dubbo SPI接口标识
 **/
@SPI
public interface JavaSpi {
    void spiTestMethod();
}
class JavaSpiTest{
    public static void main(String[] args) {
        //Java SPI
        ServiceLoader<JavaSpi> services = ServiceLoader.load(JavaSpi.class);
        for (JavaSpi item:services){
            item.spiTestMethod();
        }
        //Dubbo SPI
        ExtensionLoader<JavaSpi> loader = ExtensionLoader.getExtensionLoader(JavaSpi.class);
        JavaSpi impl1 = loader.getExtension("javaSpiImpl1");
        impl1.spiTestMethod();
    }
}