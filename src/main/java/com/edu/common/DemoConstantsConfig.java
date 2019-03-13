package com.edu.common;

import com.edu.util.zookeeper.AppDrmNode;
import com.edu.util.zookeeper.DRM;
import com.edu.util.zookeeper.DrmZookeeperClient;
import com.edu.util.zookeeper.DynamicResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: tangzh
 * @Date: 2019/1/23$ 2:37 PM$
 **/
public class DemoConstantsConfig implements DynamicResource {

    private static DrmZookeeperClient drmZookeeperClient;

    @DRM(value = "1111")
    private String cornTestDataA;
    @DRM(value = "2222")
    private String cornTestDataB;

    public void init(){
        drmZookeeperClient.configRegist(new AppDrmNode(this,"cornTestDataA","111"),true);
        drmZookeeperClient.configRegist(new AppDrmNode(this,"cornTestDataB","222"),true);
    }

    public String getCornTestDataA() {
        return cornTestDataA;
    }
    public void setCornTestDataA(String cornTestData) {
        this.cornTestDataA = cornTestData;
    }

    public String getCornTestDataB() {
        return cornTestDataB;
    }

    public void setCornTestDataB(String cornTestDataB) {
        this.cornTestDataB = cornTestDataB;
    }

    public static void main(String[] args) throws InterruptedException {

        ApplicationContext context = new ClassPathXmlApplicationContext("/xml/beanConfig.xml");
        drmZookeeperClient = (DrmZookeeperClient)context.getBean("drmZookeeperClient");
        DemoConstantsConfig config = (DemoConstantsConfig)context.getBean("demoConstantsConfig");

        for (int i=0;i<30;i++){
            System.out.println("A:"+config.getCornTestDataA());
            System.out.println("B:"+config.getCornTestDataB());
        }
    }
}
