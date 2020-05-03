package com.edu.JavaLearning.juc.aqs;



import java.util.concurrent.*;

/**
 * @Author: tangzh
 * @Date: 2019/6/22$ 10:29 AM$
 * CyclicBarrier类学习。线程栏栅,作用：
 * eg:初始化count = parties = 5,调用await count--,线程阻塞。当完成5调用后count==0。
 * 会唤醒所有等待线程并重置count = 5;循环往复
 **/
public class StuCyclicBarrier {

    private boolean isDone;

    private ExecutorService executorService =
                            new ThreadPoolExecutor(5,5,0L,TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>());

    public static void main(String[] args) throws InterruptedException {
        StuCyclicBarrier stu = new StuCyclicBarrier();
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        stu.eg();
    }

    //eg每个线程的休眠时间不同,但是总是同一时间开跑的。因为只有全部就位,下一轮才会开始
    public void eg() throws InterruptedException {
        isDone = true;
        CyclicBarrier barrier = new CyclicBarrier(5);
        System.out.println("比赛规则:5位选手,最后一位就绪后即可开始,10s后比赛结束");
        for (int i = 1; i < 6; i++) {
            executorService.execute(new Worker(i, isDone, barrier, i * 500));
        }
        Thread.sleep(8000);
        System.out.println("主线程:时间到!比赛结束");
        isDone = false;
        executorService.shutdown();
    }

    class Worker implements Runnable {

        private String name;
        private CyclicBarrier cy;
        private long sleep;

        Worker(int flag, boolean isDone, CyclicBarrier cy, long sleep) {
            this.name = String.valueOf(flag);
            this.cy = cy;
            this.sleep = sleep;
        }

        @Override
        public void run() {
            try {
                while (isDone) {
                    System.out.println("线程" + name + "就位");
                    cy.await();
                    System.out.println("线程" + name + "完成!");
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}

