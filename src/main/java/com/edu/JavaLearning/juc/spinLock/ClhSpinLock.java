package com.edu.JavaLearning.juc.spinLock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @Author: tangzh
 * @Date: 2019/6/17$ 9:15 PM$
 * CLH锁也是一种基于链表的可扩展、高性能、公平的自旋锁，
 * 申请线程只在本地变量上自旋，它不断轮询前驱的状态，如果发现前驱释放了锁就结束自旋。
 *
 * 1.从代码实现来看，CLH比MCS要简单得多。
 * 2.从自旋的条件来看，CLH是在前驱节点的属性上自旋，而MCS是在本地属性变量上自旋。
 * 3.从链表队列来看，CLH的队列是隐式的，CLHNode并不实际持有下一个节点；MCS的队列是物理存在的。
 * 4.CLH锁释放时只需要改变自己的属性，MCS锁释放则需要改变后继节点的属性。
 **/
public class ClhSpinLock {

    //保存最后一个排队结点
    private volatile Node tail;
    //updater 是对tail的引用，保证tail的原子更新
    private final static AtomicReferenceFieldUpdater<ClhSpinLock, Node> updater = AtomicReferenceFieldUpdater.newUpdater(ClhSpinLock.class, Node.class, "tail");

    //Node依靠其前置结点值进行自旋,隐式队列
    public void lock(Node current) {
        Node preNode = updater.getAndSet(this, current);
        if (preNode != null) {
            while (preNode.isBlock) {
            }
        }
    }

    public void unLock(Node current) {
        //更新失败则说明,tail!=current 即current后有人排队
        if (!updater.compareAndSet(this, current, null)) {
            //有Node排队,更新自己的状态
            current.isBlock = false;
        }
    }

    public static class Node {
        private volatile boolean isBlock = true;
    }

}
