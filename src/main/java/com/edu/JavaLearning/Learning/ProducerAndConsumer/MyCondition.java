package com.edu.JavaLearning.Learning.ProducerAndConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tangzhihao
 * @date 2018/5/15
 * 一般情况下   一个lock对象 --> 一个锁池队列
 * 多个condition  isP+isC -->lock对象的一个锁池队列
 * eg:A B C D 三个线程，目前在lock锁池队列，A:getlock+dosomething -->wait() -->
 * B:getlock+notify()[此时我只想唤醒确定的某一线程]。notify()是随机唤醒，所以没法做到。
 * 这时若A B C D 各有一个condition对象，则可以做到这点。
 */

public class MyCondition {
    private int size = 20;
    private ArrayBlockingQueue queue = new ArrayBlockingQueue(20);
    private Lock lock = new ReentrantLock();
    private Condition isP = lock.newCondition();
    private Condition isC = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        MyCondition condition = new MyCondition();
        Producer producer = condition.new Producer();
        Consumer consumer = condition.new Consumer();
        Thread thread = new Thread(producer);
        Thread thread1 = new Thread(consumer);
        thread.start();thread1.start();
        Thread.sleep(3);
        thread.interrupt();
        thread1.interrupt();
    }

    class Producer implements Runnable{

        @Override
        public void run() {
            lock.lock();
            boolean flag = true;
            int i = 0;
            try {
                while(flag){
                    if(queue.size()==size){
                        System.out.println("queue 已满！调用await()释放锁,producer进入等待队列");
                        isP.await();
                    }
                    queue.put(1);
                    isC.signal();
                    i++;
                    System.out.println("producer 产生第"+i+"个数据!");
                }
            } catch (InterruptedException e) {
                flag = false;
            } finally {
                lock.unlock();
            }
        }
    }
    class Consumer implements Runnable{

        @Override
        public void run() {
            lock.lock();
            boolean flag = true;
            int i = 0;
            try {
                while (flag){
                    if (queue.isEmpty()){
                        System.out.println("queue 为空! 调用await()释放锁,consumer进入等待队列");
                        isC.await();
                    }
                    queue.poll();
                    isP.signal();
                    i++;
                    System.out.println("consumer 消费第"+i+"个数据!");
                }
            } catch (InterruptedException e) {
                flag = false;
            } finally {
                lock.unlock();
            }
        }
    }
}
