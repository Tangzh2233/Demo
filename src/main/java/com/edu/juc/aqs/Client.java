package com.edu.juc.aqs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Tangzhihao
 * @date 2018/3/30
 */

public class Client {
    public static void main(String[] args) {
        BlockingQueue<Integer> deque = new LinkedBlockingQueue<>();
        Produer produer = new Produer(deque);
        Consumer consumer = new Consumer(deque);
        Consumer consumer1 = new Consumer(deque);
        new Thread(produer,"Producer").start();
        new Thread(consumer,"Consumer").start();
        new Thread(consumer1,"Consumer2").start();

    }
}
