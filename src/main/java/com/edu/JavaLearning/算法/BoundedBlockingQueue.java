package com.edu.JavaLearning.算法;

import com.edu.JavaLearning.juc.aqs.Produer;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/1 5:05 PM
 * 实现有限阻塞队列
 **/
public class BoundedBlockingQueue<E> {

    /**
     * 数据保存
     */
    private LinkedList<E> queue;

    /**
     * 最大容量控制
     */
    private int maxCapacity;

    /**
     * 当前容量
     */
    private int size;

    private volatile boolean flag = false;

    private ReentrantLock lock = new ReentrantLock();

    private Condition product = lock.newCondition();

    private Condition consumer = lock.newCondition();

    private Condition consumer2 = lock.newCondition();


    public static void main(String[] args) {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(10);

        //自定义阻塞队列
        new Thread(new producer(queue)).start();
        new Thread(new consumer(queue)).start();

        //交替打印0102030405
        new Thread(new Task0(queue)).start();
        new Thread(new TaskEven(queue)).start();
        new Thread(new TaskOdd(queue)).start();
    }


    public BoundedBlockingQueue(int capacity) {
        this.maxCapacity = capacity;
        queue = new LinkedList<>();
    }

    public void enqueue(E element) throws InterruptedException {
        //final保证lock不会在使用过程中被修改
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            while (size == maxCapacity) {
                product.await();
            }
            queue.addFirst(element);
            ++size;
            consumer.signal();
        } finally {
            lock.unlock();
        }
    }

    public E dequeue() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            while (size == 0) {
                consumer.await();
            }
            E e = queue.removeLast();
            --size;
            product.signal();
            return e;
        } finally {
            lock.unlock();
        }
    }

    private void zero() {
        final ReentrantLock lock = this.lock;
        while (size < maxCapacity) {
            lock.lock();
            try {
                if (!flag) {
                    System.out.println(0);
                    flag = true;
                    product.await();
                    if (size % 2 == 0) {
                        consumer.signal();
                    } else {
                        consumer2.signal();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private void even() {
        final ReentrantLock lock = this.lock;
        while (size < maxCapacity) {
            lock.lock();
            try {
                if (flag) {
                    if (size % 2 == 0) {
                        System.out.println(size == 0 ? 1 : size);
                        if (size == 0) {
                            size = 2;
                        } else {
                            size++;
                        }
                        flag = false;
                        product.signal();
                        consumer.await();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private void odd() {
        final ReentrantLock lock = this.lock;
        while (size < maxCapacity) {
            lock.lock();
            try {
                if (flag) {
                    if (size % 2 == 1) {
                        System.out.println(size);
                        size++;
                        flag = false;
                        product.signal();
                        consumer2.await();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public int size() {
        return size;
    }


    static class producer implements Runnable {

        BoundedBlockingQueue<Integer> queue;

        public producer(BoundedBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            Random random = new Random();

            while (true) {
                try {
                    int i = random.nextInt(10);
                    queue.enqueue(i);
                    System.out.println(Thread.currentThread() + "生产: " + i + "size: " + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class consumer implements Runnable {

        BoundedBlockingQueue<Integer> queue;

        public consumer(BoundedBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    System.out.println(Thread.currentThread() + "消费: " + queue.dequeue() + "size: " + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Task0 implements Runnable {

        BoundedBlockingQueue queue;

        public Task0(BoundedBlockingQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            queue.zero();
        }
    }

    static class TaskEven implements Runnable {

        BoundedBlockingQueue queue;

        public TaskEven(BoundedBlockingQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            queue.even();
        }
    }

    static class TaskOdd implements Runnable {

        BoundedBlockingQueue queue;

        public TaskOdd(BoundedBlockingQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            queue.odd();
        }
    }
}
