package com.edu.juc.aqs;

import java.util.concurrent.BlockingQueue;

/**
 * @author Tangzhihao
 * @date 2018/3/30
 */

public class Consumer implements Runnable{
    private BlockingQueue<Integer> deque;

    public Consumer(BlockingQueue deque){
        this.deque = deque;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag){
            try {
                doNext();
                /*if(deque.size()==0){
                    flag = false;
                }*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int doNext() throws InterruptedException {
        Integer poll = deque.take();
        System.out.println(Thread.currentThread().getName()+"消费："+poll.toString());
        return poll;
    }
}
