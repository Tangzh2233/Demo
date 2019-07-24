package com.edu.JavaLearning.juc.aqs;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @Author: tangzh
 * @Date: 2019/6/24$ 9:51 AM$
 * BlockingQueue:支持当获取队列元素但是队列为空时，会阻塞等待队列中有元素再返回；
 * 也支持添加元素时，如果队列已满，那么等到队列可以放入新元素时再放入
 *
 * ArrayBlockingQueue:使用ReentrantLock+Condition+Object[] 实现的安全并发容器.
 * 问题:容器的读写使用同一个ReentrantLock实例,即同一把锁。读写存在锁竞争。
 *
 * LinkedBlockingQueue:读写分离,分别设置了读锁+读线程队列和写锁+写线程队列。不存在读写的锁竞争
 **/
public class StuBlockingQueue {

    private ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(10);
    private LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    private SynchronousQueue synchronousQueue = new SynchronousQueue();
}
