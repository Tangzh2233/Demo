package com.edu.JavaLearning.jdktest.LockAndCollection;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Tangzhihao
 * @date 2018/3/28
 */

public class TheThread implements Runnable {

    private volatile int ticket = 20;
    Object lockObject = new Object();
    Lock lock = new ReentrantLock();
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public void run() {
        try {
            while (ticket > 0) {
                maipiao();
            //    readAction();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void maipiao() {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "获取锁");
        try {
            if (ticket > 0) {
                ticket--;
                System.out.println(Thread.currentThread().getName() + "正在卖票，还剩" + ticket);
            } else {
                System.out.println("已售完");
                return;
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放锁");
        }

    }

    public void maipiao1() throws InterruptedException {
        if (lock.tryLock()) {
            System.out.println(Thread.currentThread().getName() + "获取锁");
            Thread.sleep(10);
            try {
                if (ticket > 0) {
                    ticket--;
                    System.out.println(Thread.currentThread().getName() + "正在卖票，还剩" + ticket);
                } else {
                    System.out.println("已售完");
                    return;
                }
            } catch (Exception e) {

            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放锁");
            }

        }else {
            System.out.println(Thread.currentThread().getName() + "获取锁失败");
        }
    }

    public void maipiao2() throws InterruptedException {
        if (lock.tryLock(4, TimeUnit.SECONDS)) {
            System.out.println(Thread.currentThread().getName() + "获取锁");
            try {
                if (ticket > 0) {
                    ticket--;
                    System.out.println(Thread.currentThread().getName() + "正在卖票，还剩" + ticket);
                } else {
                    System.out.println("已售完");
                    return;
                }
            } catch (Exception e) {

            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放锁");
            }

        }else{
            System.out.println(Thread.currentThread().getName() + "获取锁失败");
        }
    }

    public void maipiao3() throws InterruptedException {
        lock.lockInterruptibly();
        System.out.println(Thread.currentThread().getName() + "获取锁");
        try {
            if (ticket > 0) {
                ticket--;
                System.out.println(Thread.currentThread().getName() + "正在卖票，还剩" + ticket);
            } else {
                System.out.println("已售完");
                return;
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放锁");
        }

    }

    public void readAction(){
        readWriteLock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + "获取锁");
        try {
            if (ticket > 0) {
                ticket--;
                System.out.println(Thread.currentThread().getName() + "正在卖票，还剩" + ticket);
            } else {
                System.out.println("已售完");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
            System.out.println(Thread.currentThread().getName() + "释放锁");
        }
    }
}
