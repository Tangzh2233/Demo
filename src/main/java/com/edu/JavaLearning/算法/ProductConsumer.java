package com.edu.JavaLearning.算法;

import com.edu.JavaLearning.juc.aqs.Produer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/11 10:46 PM
 **/
public class ProductConsumer {

    private static ReentrantLock lock = new ReentrantLock();
    private static BlockingQueue<Integer> queue = new LinkedBlockingDeque<>();
    private static Condition consumer = lock.newCondition();
    private static Condition consumer3 = lock.newCondition();
    private static Condition consumer5 = lock.newCondition();



    public static void main(String[] args) {
        new Thread(new Product()).start();
        new Thread(new Consumer()).start();
        new Thread(new Consumer3()).start();
        new Thread(new Consumer5()).start();
    }

    static class Product implements Runnable{

        @Override
        public void run() {
            int i = 1;
            Random random = new Random();
            while (i < 100) {
                try {
                    queue.add(i++);
                    System.out.println(Thread.currentThread().getName() + "生产：" + (i-1));
                    Thread.sleep(random.longs(10,200).iterator().nextLong());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable{

        @Override
        public void run() {
            final ReentrantLock lock1 = lock;

            while (true){
                lock1.lock();
                try {
                    Integer poll = queue.peek();
                    if(poll == null){
                        continue;
                    }
                    if(poll % 3 == 0){
                        consumer3.signal();
                        consumer.await();
                    } else if (poll % 5 == 0){
                        consumer5.signal();
                        consumer.await();
                    } else {
                        queue.poll();
                        System.out.println(Thread.currentThread().getName() + "consumer:"+poll);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock1.unlock();
                }
            }
        }
    }

    static class Consumer3 implements Runnable{

        @Override
        public void run() {
            final ReentrantLock lock1 = lock;

            while (true){
                lock1.lock();
                try {
                    Integer poll = queue.peek();
                    if(poll == null){
                        continue;
                    }
                    if(poll % 3 == 0){
                        queue.poll();
                        System.out.println(Thread.currentThread().getName() + "consumer:"+poll);
                    } else if (poll % 5 == 0){
                        consumer5.signal();
                        consumer3.await();
                    } else {
                        consumer.signal();
                        consumer3.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock1.unlock();
                }
            }
        }
    }

    static class Consumer5 implements Runnable{

        @Override
        public void run() {
            final ReentrantLock lock1 = lock;
            while (true){
                lock1.lock();
                try {
                    Integer poll = queue.peek();
                    if(poll == null){
                        continue;
                    }
                    if(poll % 3 == 0){
                        consumer3.signal();
                        consumer5.await();
                    } else if (poll % 5 == 0){
                        queue.poll();
                        System.out.println(Thread.currentThread().getName() + "consumer:"+poll);
                    } else {
                        consumer.signal();
                        consumer5.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock1.unlock();
                }
            }
        }
    }
}
