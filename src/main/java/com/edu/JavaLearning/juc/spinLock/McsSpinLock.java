package com.edu.JavaLearning.juc.spinLock;


import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @Author: tangzh
 * @Date: 2019/6/17$ 8:44 PM$
 * 是一种基于链表的可扩展、高性能、公平的自旋锁，申请线程只在本地变量(指当前线程的new Node())上自旋，
 * 直接前驱负责通知其结束自旋，从而极大地减少了不必要的处理器缓存同步的次数，降低了总线和内存的开销。
 **/
public class McsSpinLock {

    private volatile Node queue;
    //updater 是对queue的引用，保证queue的原子更新
    private static final AtomicReferenceFieldUpdater<McsSpinLock, Node> updater = AtomicReferenceFieldUpdater.newUpdater(McsSpinLock.class, Node.class, "queue");

    public void lock(Node current) {
        //将queue设置为current并返回原值
        Node preNode = updater.getAndSet(this, current);    //step1
        //preNode不为空,说明有线程排在他前边,排队,依据isBlock进行自旋
        if (preNode != null) {
            preNode.next = current;                             //step2
            while (current.isBlock) {                           //step3
            }
        } else {//当前Node为头节点,修改自旋状态为非阻塞
            current.isBlock = false;
        }
    }

    public void unLock(Node current) {
        if (current.isBlock) {
            return;
        }
        //检查当前结点后是否有排队
        if (current.next == null) {
            if (updater.compareAndSet(this, current, null)) {
                //无人排队,queue置空
                return;
            } else {
                //突然有人排队,自旋等待后续结点执行至step2结束自旋
                while (current.next == null) {
                }
            }
        }
        //唤醒后续结点
        current.next.isBlock = false;
        //help gc
        current.next = null;
    }

    /**
     * 当前线程通过判断自己的isBlock属性跳出自旋
     * 所以使用volatile关键字
     */
    public static class Node {
        private volatile Node next;
        private volatile boolean isBlock = true;
    }
}
