package com.edu.JavaLearning.Learning.ProducerAndConsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * @author Tangzhihao
 * @date 2018/3/30
 * 对于不能立即满足但可能在将来某一时刻可以满足的操作，
 * 这四种形式的处理方式不同：第一种是抛出一个异常，
 * 第二种是返回一个特殊值（null 或 false，具体取决于操作），
 * 第三种是在操作可以成功前，无限期地阻塞当前线程，
 * 第四种是在放弃前只在给定的最大时间限制内阻塞。
 *  抛出异常    特殊值     阻塞      超时
插入 add(e)    offer(e)  put(e)    offer(e, time, unit)
移除 remove()  poll()    take()    poll(time, unit)
检查 element() peek()    不可用     不可用

 */

public class Produer implements Runnable{
    private BlockingQueue<Integer> deque;

    public Produer(BlockingQueue deque){
        this.deque = deque;
    }

    @Override
    public void run() {
        int i = 0;
        while (i<100){
            try {
                doNext();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
    public void doNext() throws InterruptedException {
        int i = new Random().nextInt(100);
        deque.put(i);
        System.out.println(Thread.currentThread().getName()+"生产："+i);
    }
}
