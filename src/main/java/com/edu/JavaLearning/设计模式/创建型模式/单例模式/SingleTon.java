package com.edu.JavaLearning.设计模式.创建型模式.单例模式;



/**
 * @author Tangzhihao
 * @date 2018/4/24
 * 饿汉式--线程安全，资源利用率不高。如果getInstace永远不调用，则内存中的实例是多余的。
 * 静态内部类中的静态变量用到的时候才加载
 */

public class SingleTon {
    private static SingleTon singleTon = new SingleTon();

    public static SingleTon getInstance(){
        return singleTon;
    }

    /**
     * @author: Tangzhihao
     * @date: 2018/4/24
     * @description:静态内部类的方式构建，解决饿汉式的缺点，并且线程安全
     */
    public static class SingleTonHelper{
        static SingleTon singleTonC = new SingleTon();
    }
    public static SingleTon getInstanceC(){
        return SingleTonHelper.singleTonC;
    }
}
