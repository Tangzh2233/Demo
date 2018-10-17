package com.edu.JavaLearning.Learning.Executor框架;


import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * @author Tangzhihao
 * @date 2018/3/1
 *
 * 参考资料：
 * https://mp.weixin.qq.com/s/buR4X94v1xSpkVXA5N9s1g
 * https://www.jianshu.com/p/ae67972d1156
 *
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
//        FutureTask<String> task = new FutureTask<>(new MyCallableTask());
//        new Thread(task).start();
//        System.out.println(task.isDone());
//        System.out.println(task.get());
//
//        System.out.println(new DecimalFormat("_0000").format(222));
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        },3600,TimeUnit.SECONDS);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

            }
        };

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
    public static ThreadPoolExecutor myThreadPoolExecutor(){
        return new ThreadPoolExecutor(
                5,
                10,
                1000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
