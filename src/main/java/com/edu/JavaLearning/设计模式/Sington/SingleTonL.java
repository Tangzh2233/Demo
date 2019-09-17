package com.edu.JavaLearning.设计模式.Sington;

import java.math.BigDecimal;

/**
 * @author Tangzhihao
 * @date 2018/4/24
 * 懒汉式
 *  优点：避免了饿汉式的那种在没有用到的情况下创建事例，资源利用率高，不执行getInstance()就不会被实例，可以执行该类的其他静态方法
 *  缺点：懒汉式在单个线程中没有问题，但多个线程同事访问的时候就可能同事创建多个实例，而且这多个实例不是同一个对象，虽然后面创建的实例会覆盖先创建的实例，但是还是会存在拿到不同对象的情况。解决这个问题的办法就是加锁synchonized，第一次加载时不够快，多线程使用不必要的同步开销大。
 */

public class SingleTonL {
    private static SingleTonL singleinstacne = null;
    private static final Object lock = new Object();
    private SingleTonL(){}
    public static SingleTonL getInstance(){
        //多线程时此处会有问题。解决见下
        if(singleinstacne==null){
            singleinstacne = new SingleTonL();
        }
        return singleinstacne;
    }

    //方法加锁,线程安全。但是相比较getInstance2的doubleCheck方式
    //效率低。并且静态方法加锁,直接作用于class,锁粒度过大
    public static synchronized SingleTonL getInstance1(){
        if(singleinstacne == null){
            singleinstacne = new SingleTonL();
        }
        return singleinstacne;
    }

    //解决多线程问题
    //doubleCheck的好处是减小锁粒度,非直接在方法上加锁
    //此处synchronized加一个Object锁实例,比直接使用SingleTonL.class锁粒度更小一些
    public static SingleTonL getInstance2(){
        if(singleinstacne==null){
            synchronized (lock){
                if(singleinstacne==null){
                    singleinstacne = new SingleTonL();
                }
            }
        }
        return singleinstacne;
    }
}
