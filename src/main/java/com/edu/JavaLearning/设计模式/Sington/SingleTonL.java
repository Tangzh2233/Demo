package com.edu.JavaLearning.设计模式.Sington;

/**
 * @author Tangzhihao
 * @date 2018/4/24
 * 懒汉式
 *  优点：避免了饿汉式的那种在没有用到的情况下创建事例，资源利用率高，不执行getInstance()就不会被实例，可以执行该类的其他静态方法
 *  缺点：懒汉式在单个线程中没有问题，但多个线程同事访问的时候就可能同事创建多个实例，而且这多个实例不是同一个对象，虽然后面创建的实例会覆盖先创建的实例，但是还是会存在拿到不同对象的情况。解决这个问题的办法就是加锁synchonized，第一次加载时不够快，多线程使用不必要的同步开销大。
 */

public class SingleTonL {
    private static SingleTonL singleinstacne = null;
    private SingleTonL(){}
    public static SingleTonL getInstance(){
        //多线程时此处会有问题。解决见下
        if(singleinstacne==null){
            singleinstacne = new SingleTonL();
        }
        return singleinstacne;
    }
    //解决多线程问题
    public static SingleTonL getInstance2(){
        if(singleinstacne==null){
            synchronized (SingleTonL.class){
                if(singleinstacne==null){
                    singleinstacne = new SingleTonL();
                }
            }
        }
        return singleinstacne;
    }
}
