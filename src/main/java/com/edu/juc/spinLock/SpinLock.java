package com.edu.juc.spinLock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tangzh
 * @Date: 2019/6/17$ 7:59 PM$
 * @link: https://coderbee.net/index.php/concurrent/20131115/577
 * 原理:自旋锁是指当一个线程尝试获取某个锁时，如果该锁已被其他线程占用，就一直循环检测锁是否被释放，而不是进入线程挂起或睡眠状态。
 * 自旋锁适用于锁保护的临界区很小的情况，临界区很小的话，锁占用的时间就很短。
 * 不适合长时间占用锁资源
 *
 * eg:简单自旋锁实现,没法保证公平性
 **/
public class SpinLock {

    private AtomicInteger state = new AtomicInteger(0);
    private static int count = 10000;

    public void lock() {
        while (!state.compareAndSet(0, 1)) {
        }
    }

    public void unLock() {
        state.compareAndSet(1, 0);
    }

    public static void main(String[] args) throws InterruptedException {
        ClhSpinLock lock = new ClhSpinLock();
        CountDownLatch latch = new CountDownLatch(10);
        for(int i=0;i<10;i++){
            for(int j=0;j<1001;j++){
                new Thread(() -> {
                    ClhSpinLock.Node current = new ClhSpinLock.Node();
                    try{
                        lock.lock(current);
                        if(count == 0){
                            System.out.println("已售尽");
                            return;
                        }
                        count--;
                        System.out.println(Thread.currentThread()+"售第"+count+"张票");
                    }finally {
                        lock.unLock(current);
                    }

                }).start();
            }
            latch.countDown();
        }
        latch.await();
    }
}
