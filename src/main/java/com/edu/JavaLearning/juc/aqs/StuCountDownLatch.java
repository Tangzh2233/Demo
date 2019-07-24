package com.edu.JavaLearning.juc.aqs;

import java.util.concurrent.*;

/**
 * @Author: tangzh
 * @Date: 2019/6/22$ 11:11 AM$
 * downLatch = new CountDownLatch(5)
 * eg1:所有子任务完成以后主任务才会返回
 * eg2:signal保证子线程阻塞在主线程countDown之前;done保证了主线程在子线程完成之后才会返回
 **/
public class StuCountDownLatch {

    public static void main(String[] args) throws InterruptedException {
        StuCountDownLatch countDownLatch = new StuCountDownLatch();
        //eg1
        countDownLatch.eg1();
        //eg2
        countDownLatch.eg2();
    }


    //eg1
    public void eg1() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(5);
        ExecutorService executorService =
                        new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Worker(i, downLatch));
        }
        downLatch.await();
        System.out.println("比赛结束！");
        executorService.shutdown();
    }

    class Worker implements Runnable {

        private String name;
        private CountDownLatch latch;

        Worker(int name, CountDownLatch latch) {
            this.name = String.valueOf(name);
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println("线程" + name + "完成");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //此处若若主线程不休眠1s存在,子线程还没就位,主线程已经countDown了。
    //这种场景使用CyclicBarrier更合适
    public void eg2() throws InterruptedException {
        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(5);
        ExecutorService executorService =
                        new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Worker2(i, signal, done));
        }
        Thread.sleep(1000);
        System.out.println("主线程：1s就位时间等待结束。比赛开始！");
        signal.countDown();
        done.await();
        System.out.println("比赛结束");
        executorService.shutdown();
    }

    //eg2
    class Worker2 implements Runnable {

        private String name;
        private CountDownLatch startSignal;
        private CountDownLatch doneSignal;

        Worker2(int name, CountDownLatch start, CountDownLatch done) {
            this.name = String.valueOf(name);
            this.startSignal = start;
            this.doneSignal = done;
        }

        @Override
        public void run() {
            try {
                System.out.println("线程" + name + "就位!");
                startSignal.await();
                Thread.sleep(1000);
                System.out.println("线程" + name + "完成!");
                doneSignal.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
