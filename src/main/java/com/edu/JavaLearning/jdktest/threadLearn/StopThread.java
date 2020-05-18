package com.edu.JavaLearning.jdktest.threadLearn;


/**
 * @author Tangzhihao
 * @date 2017/10/17
 */

public class StopThread {

    private final static Object lock = new Object();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            String name = Thread.currentThread().getName();
            synchronized (lock){
                try {
                    System.out.println(name + "sleep");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(name + "响应中断");
                }
                System.out.println(name);
            }
        });

        Thread t2 = new Thread(() -> {
            String name = Thread.currentThread().getName();
            synchronized (lock){
                try {
                    System.out.println(name + "wait");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(name + "响应中断");
                }
                System.out.println(name);
            }
        });

        t1.start();
        t2.start();

        t1.interrupt();
        t2.interrupt();

    }

}
