package com.edu.JavaLearning.设计模式.创建型模式.单例模式;


/**
 * @author Tangzhihao
 * @date 2018/4/24
 * 懒汉式
 * 优点：避免了饿汉式的那种在没有用到的情况下创建事例，资源利用率高，不执行getInstance()就不会被实例，可以执行该类的其他静态方法
 * 缺点：懒汉式在单个线程中没有问题，但多个线程同事访问的时候就可能同事创建多个实例，而且这多个实例不是同一个对象，虽然后面创建的实例会覆盖先创建的实例，但是还是会存在拿到不同对象的情况。解决这个问题的办法就是加锁synchonized，第一次加载时不够快，多线程使用不必要的同步开销大。
 * <p>
 * volatile保证了对类变量的写操作完成之前不可被读。
 * 因为volatile的禁止指令重排序，初始化instance对象并非原子操作，
 * 它包括：1.开辟堆内存2.调用构造方法初始化对象3.将instance指向新对象；
 * 如果指令重排成 1-3-2
 * 如果没有volatile关键字，且在并发情况下，如果某个线程完成了1 3两个步骤，2未执行
 * 还未给instance变量内存空间赋值，此时另一个线程进入外层判空后后发现instance对象非空，实际内存空间是空的，
 * 就返回了未构造完全的instance对象，导致空指针异常；
 * volatile的意义在于能够禁止对当前对象进行指令的重排序，也就是happen-before原则的关于volatile的一条：
 * "volatile变量规则：对一个变量的写操作happen before于后面对这个变量的读操作"，
 * 也就是说，无论什么情况，对于volatile变量读取必须在写操作完成后才能读取，不能暴露写操作的中间状态。
 * 所以不会出现未完成构造就读取的情况；但是volatile不能保证同时对变量的写操作也是有序的，也就是volatile不能保证原子性。
 */

public class SingleTonL {
    private volatile static SingleTonL singleInstance = null;
    private static final Object lock = new Object();

    private SingleTonL() {
    }

    public static SingleTonL getInstance() {
        //多线程时此处会有问题。解决见下
        if (singleInstance == null) {
            singleInstance = new SingleTonL();
        }
        return singleInstance;
    }

    //方法加锁,线程安全。但是相比较getInstance2的doubleCheck方式
    //效率低。并且静态方法加锁,直接作用于class,锁粒度过大
    public static synchronized SingleTonL getInstance1() {
        if (singleInstance == null) {
            singleInstance = new SingleTonL();
        }
        return singleInstance;
    }

    //解决多线程问题
    //doubleCheck的好处是减小锁粒度,非直接在方法上加锁
    //此处synchronized加一个Object锁实例,比直接使用SingleTonL.class锁粒度更小一些
    //使用volatile修饰
    public static SingleTonL getInstance2() {
        if (singleInstance == null) {
            synchronized (lock) {
                if (singleInstance == null) {
                    //非原子操作,若有指令重排,会存在一定问题
                    singleInstance = new SingleTonL();
                }
            }
        }
        return singleInstance;
    }

    public static SingleTonL getInstance3() {
        if (singleInstance == null) {
            synchronized (lock) {
                if (singleInstance == null) {
                    try {
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        Class<?> aClass = loader.loadClass(SingleTonL.class.getName());
                        singleInstance = (SingleTonL)aClass.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create an instance of " + SingleTonL.class.getName(),e);
                    }
                }
            }
        }
        return singleInstance;
    }
}
