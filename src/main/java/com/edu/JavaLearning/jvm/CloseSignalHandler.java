package com.edu.JavaLearning.jvm;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/7/29 16:24
 * @description
 **/
public class CloseSignalHandler implements SignalHandler {


    /**
     * 信号量执行逻辑
     * @param signal
     */
    @Override
    public void handle(Signal signal) {
        System.out.println(signal.getName() + " is Received");
        stopAll();
    }

    public static void main(String[] args) throws Exception {

        //注册信号量
        CloseSignalHandler signalHandler = new CloseSignalHandler();
        // kill -2 PID 捕捉ctrl+c命令
        Signal.handle(new Signal("INT"),signalHandler);
        // kill -15 PID
        Signal.handle(new Signal("TERM"),signalHandler);
        // kill -6 PID
        Signal.handle(new Signal("ABRT"),signalHandler);

        //注册Jvm hook
        Runtime.getRuntime().addShutdownHook(new Thread(CloseSignalHandler::stopAll));

        System.out.println("do something");
        Thread.sleep(2000);
        System.out.println("main exiting");
    }

    public synchronized static void stopAll(){
        System.out.println("stop all ing");
    }
}
