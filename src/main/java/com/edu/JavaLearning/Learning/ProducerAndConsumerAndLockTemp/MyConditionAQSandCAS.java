package com.edu.JavaLearning.Learning.ProducerAndConsumerAndLockTemp;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tangzhihao
 * @date 2018/5/15
 *
 * ===================Condition====================
 * 一般情况下   一个lock对象 --> 一个锁池队列
 * 多个condition  isP+isC -->lock对象的一个锁池队列
 * eg:A B C D 三个线程，目前在lock锁池队列，A:getlock+dosomething -->wait() -->
 * B:getlock+notify()[此时我只想唤醒确定的某一线程]。notify()是随机唤醒，所以没法做到。
 * 这时若A B C D 各有一个condition对象，则可以做到这点。
 * ======个人理解======
 * 每一个condition作为一个对象，维持了一套等待队列和同步队列，这些队列对应同一个lock。这样可以将线程通过condition分类,可以选择唤醒对应等待队列的线程
 * 相当于之前，Synchronized(Obj),多了condition套队列。
 *
 * ===================CAS========================
 * CAS[CompareAndSwap(V,E,N)]比较替换
 *
 * cas的意思是：在线程一操作此变量的时候，此变量应该没被其他线程更改过，即V==E 否则就不做任何操作。
 * compare step1从内存中取的值与step2从内存中取的值是否一致。volatile修饰的值从主存中取值
 * public final int getAndAddInt(Object var1, long var2, int var4) {
 *     int var5;
 *     do {
 *         //step1
 *         var5 = this.getIntVolatile(var1, var2);//从主存中拿到变量当前值
 *     } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));//step2
 *     return var5;
 * }
 * 假设线程A和线程B同时执行getAndAdd操作（分别跑在不同CPU上）：
 *
 * 1.AtomicInteger里面的value原始值为3，即主内存中AtomicInteger的value为3，
 *   根据Java内存模型，线程A和线程B各自持有一份value的副本，值为3。
 * 2.线程A通过getIntVolatile(var1, var2)拿到value值3，这时线程A被挂起。
 * 3.线程B也通过getIntVolatile(var1, var2)方法获取到value值3，运气好，线程B没有被挂起，
 *   并执行compareAndSwapInt方法比较内存值也为3，成功修改内存值为2。
 * 4.这时线程A恢复，执行compareAndSwapInt方法比较，发现自己手里的值(3)和内存的值(2)不一致，
 *   说明该值已经被其它线程提前修改过了，那只能重新来一遍了。
 * 5.重新获取value值，因为变量value被volatile修饰，所以其它线程对它的修改，线程A总是能够看到，
 *   线程A继续执行compareAndSwapInt进行比较替换，直到成功。
 *
 * ========================AQS========================
 *
 */

public class MyConditionAQSandCAS {
    private int size = 10;
    private ArrayBlockingQueue queue = new ArrayBlockingQueue(size);
    private static Lock lock = new ReentrantLock();
    private Mutex mutex = new Mutex();
    private static Condition isP = lock.newCondition();
    private static Condition isC = lock.newCondition();
    private static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException{
        MyConditionAQSandCAS condition = new MyConditionAQSandCAS();
        Producer producer = condition.new Producer();
        Consumer consumer = condition.new Consumer();
        Thread p1 = new Thread(producer,"P1");
        Thread c1 = new Thread(consumer,"C1");
        Thread p2 = new Thread(producer,"P2");
        Thread c2 = new Thread(consumer,"C2");
        c1.start();
        c2.start();p1.start();p2.start();
        Thread.sleep(2);
        p1.interrupt();
        p2.interrupt();
        c1.interrupt();
        c2.interrupt();
        System.out.println(atomicInteger);
    }

    class Producer implements Runnable{
        boolean flag = true;
        @Override
        public void run() {
            atomicInteger.getAndAdd(1);
            lock.lock();
            int i = 0;
            try {
                while(flag){
                    if(queue.size()==size){
                        System.out.println("queue 已满！调用await()释放锁,"+Thread.currentThread().getName()+"进入等待队列");
                        //当前线程进入等待队列
                        isP.await();
                    }
                    queue.put(1);
                    isC.signal();//唤醒consumer
                    i++;
                    System.out.println(Thread.currentThread().getName()+"产生第"+i+"个数据!");
                }
            } catch (InterruptedException e) {
                flag = false;
            } finally {
                lock.unlock();
            }
        }
    }

    class Consumer implements Runnable{
        boolean flag = true;

        @Override
        public void run() {
            lock.lock();
            int i = 0;
            try {
                while (flag){
                    if (queue.isEmpty()){
                        System.out.println("queue 为空! 调用await()释放锁,"+Thread.currentThread().getName()+"进入等待队列");
                        isC.await();
                    }
                    queue.poll();
                    isP.signal();//唤醒producer
                    i++;
                    System.out.println(Thread.currentThread().getName()+"消费第"+i+"个数据!");
                }
            } catch (InterruptedException e) {
                flag = false;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
      * @description:获取Unsafe实例
    **/
    public static Unsafe getUnSafe(){
        Class<?> aClass;
        Unsafe instance = null;
        try {
            aClass = Class.forName("sun.misc.Unsafe");
            Constructor constructor = aClass.getDeclaredConstructor((Class<?>) null);
            //忽略修饰符，暴力访问
            constructor.setAccessible(true);
            instance = (Unsafe) constructor.newInstance((Object) null);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

}
