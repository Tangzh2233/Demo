package com.edu.JavaLearning.设计模式.Sington;


/**
 * @author Tangzhihao
 * @date 2018/4/24
 * 懒汉式
 *  优点：避免了饿汉式的那种在没有用到的情况下创建事例，资源利用率高，不执行getInstance()就不会被实例，可以执行该类的其他静态方法
 *  缺点：懒汉式在单个线程中没有问题，但多个线程同事访问的时候就可能同事创建多个实例，而且这多个实例不是同一个对象，虽然后面创建的实例会覆盖先创建的实例，但是还是会存在拿到不同对象的情况。解决这个问题的办法就是加锁synchonized，第一次加载时不够快，多线程使用不必要的同步开销大。
 *
 *  volatile保证了对类变量的写操作完成之前不可被读。
 *  因为volatile的禁止指令重排序，初始化instance对象并非原子操作，
 *  它包括：1.开辟堆内存2.调用构造方法初始化对象3.将instance指向新对象；
 *  如果没有volatile关键字，且在并发情况下，如果某个线程完成了1 2两个步骤，
 *  还未给instance变量赋值，此时另一个线程进入外层判空后后发现instance对象非空，
 *  就返回了未构造完全的instance对象，导致空指针异常；volatile的意义在于能够禁止对当前对象进行指令的重排序，
 *  也就是happen-before原则的关于volatile的一条：
 *  "volatile变量规则：对一个变量的写操作happen before于后面对这个变量的读操作"，
 *  也就是说，无论什么情况，对于volatile变量的写操作必须在完成后才能读取，不能暴露写操作的中间状态。
 *  所以不会出现未完成构造就读取的情况；但是volatile不能保证同时对变量的写操作也是有序的，也就是volatile不能保证原子性。
 */

public class SingleTonL {
    private volatile static SingleTonL singleinstacne = null;
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
    //使用volatile修饰
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
