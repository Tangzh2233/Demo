package com.edu.JavaLearning.juc.aqs;

import java.util.concurrent.*;

/**
 * @Author: tangzh
 * @Date: 2019/6/22$ 3:18 PM$
 * 套路解读：创建 Semaphore 实例的时候，需要一个参数 permits，
 * 这个基本上可以确定是设置给 AQS 的 state 的，然后每个线程调用 acquire 的时候，
 * 执行 state = state - 1，release 的时候执行 state = state + 1，
 * 当然，acquire 的时候，如果 state = 0，说明没有资源了，需要等待其他线程 release。
 * 一般用来控制资源访问
 *
 * eg:某一资源规定最多只能用10条线程同时访问
 **/
public class StuSemaphore {

    private Semaphore available = new Semaphore(10);
    private boolean isDone;

    public static void main(String[] args) throws InterruptedException {
        StuSemaphore stuSemaphore = new StuSemaphore();
        stuSemaphore.eg();
    }

    public void eg() {
        ExecutorService executorService =
                new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Worker(i));
        }
        isDone = true;
        executorService.shutdown();
    }


    class Worker implements Runnable {
        private String name;

        Worker(int name) {
            this.name = String.valueOf(name);
        }
        @Override
        public void run() {
            try {
                while (!isDone){
                    available.acquire();
                    System.out.println("线程" + name + "正在访问资源"+"\n当前还可允许:"+available.availablePermits());
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {

            } finally {
                available.release();
            }
        }
    }

}
