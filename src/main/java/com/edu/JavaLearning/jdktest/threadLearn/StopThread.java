package com.edu.JavaLearning.jdktest.threadLearn;

/**
 * @author Tangzhihao
 * @date 2017/10/17
 */

public class StopThread extends Thread{


    @Override
    public void run() {
        for(int i=0;i<5000;i++){
            if(this.isInterrupted()){
                System.out.println("return 返回，线程停止!");
                return;
            }
        }
        /*@2:try {
            System.out.println("run begin");
            *//*@1:Thread.sleep(200000);*//*

            for(int i=0;i<5000;i++){
                if(this.isInterrupted()){
                    throw new InterruptedException();
                }
            }
            System.out.println("run end");
        } catch (InterruptedException e) {
            *//*@1:System.out.println("沉睡中停止，进入异常"+this.isInterrupted());*//*
            System.out.println("throw Exception。进入异常");
            e.printStackTrace();
        }*/
        System.out.println(Thread.currentThread().getName());
    }
}
