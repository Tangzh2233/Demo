package com.edu.JavaLearning.Executor框架;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Tangzhihao
 * @date 2018/3/1
 * 参考资料：
 * https://mp.weixin.qq.com/s/buR4X94v1xSpkVXA5N9s1g
 * https://www.jianshu.com/p/ae67972d1156
 */

public class ExecutorInit {
    private Executor executor;
    private ExecutorService executorService;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ThreadPoolExecutor threadPoolExecutor;

    public void initExecutors() {
        //创建固定大小的线程池
        ExecutorService fixPool = Executors.newFixedThreadPool(10);
        //创建一个单线程的Executor(执行者)
        ExecutorService singlePool = Executors.newSingleThreadExecutor();
        //创建一个无上限的线程池。实际上也是Integer.MAX_VALUE
        ExecutorService cachePool = Executors.newCachedThreadPool();
        //创建延迟执行和周期执行任务的线程池。
        ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(1);

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
//        ExecutorInit init = new ExecutorInit();
//    //    init.initFixedPool();
//
//
//        //--1--运行Callable线程，和获取返回结果
//        FutureTask<String> task = new FutureTask<>(new MyCallableTask());
//        new Thread(task).start();
//        System.out.println(task.isDone());
//        System.out.println(task.get());
//
//        System.out.println(new DecimalFormat("_0000").format(222));
//        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
//        executor.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        },3600,TimeUnit.SECONDS);
//
//        TimerTask task1 = new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        };
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,2,0L,TimeUnit.MICROSECONDS,new ArrayBlockingQueue<>(5));
//        for(int i=0;i<10;i++){
//            executor.execute(new TestThread());
//        }
//        executor.shutdown();
        executorCompletionServiceTest();
    }

    /**
     * @author: Tangzhihao
     * @date: 2018/3/1 19:09
     * @description:创建固定大小线程池
     */
    public void initFixedPool() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        System.out.println("=========fixPool Start============");
        for (int i = 0; i < 10; i++) {
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

    /**
     * Callable使用
     * 使用ExecutorService提交Callable任务时,结果future.get()若当前任务未完成，会阻塞至任务完成
     * 问题：阻塞过程即等待任务完成过程,eg:list便利到task1,但是task1完成,然而task2已经完成了,此时
     * 我们想要的结果应该是返回task2结果，而不是等待task1完成。
     * <p>
     * 解决：ExecutorCompletionService,会将完成的结果放在队列中,service.take()会优先返回已完成的task
     * 实现方式:Callable任务会被线程池包装成FutureTask任务,执行任务实际执行时调用的是FutureTask.run()
     * FutureTask会对Callable进行增强,在任务完成时会调用finishCompletion()方法，其中存在一个done(),
     * 方法给子类重写。即子类可以通过done方法知道,任务已完成并做后续自己的业务处理
     */
    public static void callableTest() throws ExecutionException, InterruptedException {
        List<Future<String>> futureResult = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 20; i++) {
            Future<String> future = executorService.submit(new MyCallableTask());
            futureResult.add(future);
        }
        for (Future<String> future : futureResult) {
            System.out.println(future.get());
        }
        executorService.shutdown();
    }

    public static void executorCompletionServiceTest() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
        for (int i = 0; i < 20; i++) {
            completionService.submit(new MyCallableTask());
        }
        for (int i = 0; i < 20; i++) {
            System.out.println(completionService.take().get());
        }
        executorService.shutdown();
    }

    static class TestThread implements Runnable {

        @Override
        public void run() {
            System.out.println("=========Thread Start==========");
            //    for (int i=0;i<4;i++){
            System.out.println("我是线程" + Thread.currentThread().getId());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //    }
        }
    }

    static class MyCallableTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName() + "is end";
        }
    }

    /**
     * corePoolSize:核心线程数
     * maximumPoolSize:最大线程数
     * keepAlivetime:非核心线程,闲置时长
     * TimeUnit:闲置时长单位
     * BlockingQueue<>:Task的等待队列
     * ThreadFactory:
     * RejectedExecutionHandler:
     **/

    /**
     * 当一个任务被添加进线程池时，执行策略：
     * 1.线程数量未达到corePoolSize，则新建一个线程(核心线程)执行任务
     * 2.线程数量达到了corePools，则将任务移入队列等待
     * 3.队列已满，新建线程(非核心线程)执行任务
     * 4.队列已满，总线程数又达到了maximumPoolSize，就会由(RejectedExecutionHandler)抛出异常
     **/
    public static ThreadPoolExecutor myThreadPoolExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Test-Pool-%d").build();
        return new ThreadPoolExecutor(
                5,
                10,
                1000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }
}
