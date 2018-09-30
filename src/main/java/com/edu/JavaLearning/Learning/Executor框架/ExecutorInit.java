package com.edu.JavaLearning.Learning.Executor框架;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.text.DecimalFormat;
import java.util.concurrent.*;

/**
 * @author Tangzhihao
 * @date 2018/3/1
 * description Executos Learning
 */

public class ExecutorInit {
    private Executor executor;
    private ExecutorService executorService;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ThreadPoolExecutor threadPoolExecutor;

    public void initExecutors(){
        //创建固定大小的线程池
        ExecutorService fixPool = Executors.newFixedThreadPool(10);
        //创建一个单线程的Executor(执行者)
        ExecutorService singlePool = Executors.newSingleThreadExecutor();

        ExecutorService cachePool = Executors.newCachedThreadPool();

        ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(1);

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorInit init = new ExecutorInit();
    //    init.initFixedPool();

        //--1--运行Callable线程，和获取返回结果
        FutureTask<String> task = new FutureTask<>(new MyCallableTask());
        new Thread(task).start();
        System.out.println(task.isDone());
        System.out.println(task.get());

        System.out.println(new DecimalFormat("_0000").format(222));

    }

    /**
     * @author: Tangzhihao
     * @date: 2018/3/1 19:09
     * @description:创建固定大小线程池
     */
    public void initFixedPool() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        System.out.println("=========fixPool Start============");
        for (int i=0;i<10;i++) {
            fixedPool.execute(new TestThread());
            Future<?> future = fixedPool.submit(new TestThread());
            //--2--使用Executor的方式运行实现Callable接口的线程
            Future<String> future1 = fixedPool.submit(new MyCallableTask());
            //试图取消任务
            future.cancel(true);
            //在一定时间内尝试获取执行结果
            Object obj = future.get(2000L, TimeUnit.SECONDS);
        }
        fixedPool.shutdown();
    }

    class TestThread implements Runnable{

        @Override
        public void run() {
            System.out.println("=========Thread Start==========");
        //    for (int i=0;i<4;i++){
            System.out.println("我是线程"+Thread.currentThread().getId());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //    }
        }
    }
    static class MyCallableTask implements Callable<String>{

        @Override
        public String call() throws Exception {
            System.out.println("=========Thread Start==========");
            //    for (int i=0;i<4;i++){
            System.out.println("我是线程"+Thread.currentThread().getId());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Thread is end";
        }
    }
}
