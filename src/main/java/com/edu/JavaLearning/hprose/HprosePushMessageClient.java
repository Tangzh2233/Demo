package com.edu.JavaLearning.hprose;
import hprose.client.HproseTcpClient;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/30 5:23 PM
 **/
public class HprosePushMessageClient {


    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("START");
        HproseTcpClient client = new HproseTcpClient("tcp://127.0.0.1:8888");
        IHello helloClient = client.useService(IHello.class);
        System.out.println(helloClient.hello("World"));
        System.out.println("END");
    }
}


interface IHello {
    String hello(String name);
}

