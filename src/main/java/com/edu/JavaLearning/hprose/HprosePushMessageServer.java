package com.edu.JavaLearning.hprose;

import hprose.server.HproseTcpServer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/30 5:23 PM
 **/
public class HprosePushMessageServer {

    public static String hello(String name) {
        return "Hello " + name + "!";
    }

    private static AtomicInteger atomicInteger = new AtomicInteger(0);


    public static void main(String[] args) throws Exception {
        HproseTcpServer server = new HproseTcpServer("tcp://127.0.0.1:8888");
        server.add("hello",HprosePushMessageServer.class);
        server.start();

        System.out.println("server start");
        System.in.read();
        System.out.println("server stop");


    }
}
