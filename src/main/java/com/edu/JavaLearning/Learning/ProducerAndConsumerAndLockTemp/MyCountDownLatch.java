package com.edu.JavaLearning.Learning.ProducerAndConsumerAndLockTemp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: tangzh
 * @Date: 2019/1/16$ 3:52 PM$
 **/
public class MyCountDownLatch {

    private static CountDownLatch countDownLatch;
    private static ExecutorService executorService;

    static {
        countDownLatch = new CountDownLatch(5);
        executorService = Executors.newFixedThreadPool(5);
    }

    public static void main(String[] args) throws InterruptedException {
        List<Job> jobList = new ArrayList<>();
        for(int i=1;i<=5;i++){
            jobList.add(new Job(countDownLatch,i*1000));
        }
        for(Job item: jobList){
            executorService.execute(item);
        }
        countDownLatch.await();
        System.out.println("主线程执行完成");
    }


}
class Job implements Runnable{
    private CountDownLatch cdl;
    private long time;

    public Job(CountDownLatch cdl,long time){
        this.cdl = cdl;
        this.time = time;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(time);
            System.out.println(Thread.currentThread().getName()+"执行完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            cdl.countDown();
        }
    }
}
