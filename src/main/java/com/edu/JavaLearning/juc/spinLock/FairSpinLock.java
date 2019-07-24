package com.edu.JavaLearning.juc.spinLock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tangzh
 * @Date: 2019/6/17$ 8:26 PM$
 * eg:尝试保证公平的自旋锁
 *
 * 虽然解决了公平性的问题，但是多处理器系统上，
 * 每个进程/线程占用的处理器都在读写同一个变量state,每次读写操作都必须在多个处理器缓存之间进行缓存同步，
 * 这会导致繁重的系统总线和内存的流量，大大降低系统整体的性能。
 * [AQS同样如此,但是它会进行线程挂起和睡眠,降低了这个问题!]
 **/
public class FairSpinLock {

    //当前可执行的号
    private AtomicInteger state = new AtomicInteger(0);
    //号分发
    private AtomicInteger ticket = new AtomicInteger(0);

    public int lock() {
        //拿个号
        int ticket = this.ticket.getAndIncrement();
        //当前状态不是自己的号则自旋
        while (state.get() != ticket) {
        }
        return ticket;
    }

    public void unLock(int ticket) {
        //释放锁 state+1 即更新state为下一个号
        int next = ticket + 1;
        state.compareAndSet(ticket, next);
    }


}
