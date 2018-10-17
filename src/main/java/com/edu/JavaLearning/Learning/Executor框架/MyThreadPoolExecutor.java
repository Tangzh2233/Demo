package com.edu.JavaLearning.Learning.Executor框架;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @Author: tangzh
 * @Date: 2018/10/12$ 下午3:07$
 * /**
 *       * corePoolSize:核心线程数
 *       * maximumPoolSize:最大线程数
 *       * keepAlivetime:非核心线程,闲置时长
 *       * TimeUnit:闲置时长单位
 *       * BlockingQueue<>:Task的等待队列
 *       * ThreadFactory:
 *       * RejectedExecutionHandler:
  *       当一个任务被添加进线程池时，执行策略：
  *      * 1.线程数量未达到corePoolSize，则新建一个线程(核心线程)执行任务
  *      * 2.线程数量达到了corePools，则将任务移入队列等待
  *      * 3.队列已满，新建线程(非核心线程)执行任务
  *      * 4.队列已满，总线程数又达到了maximumPoolSize，就会由(RejectedExecutionHandler)抛出异常
  *
  *   异常策略，
  *   ThreadPoolExecutor.new AbortPolicy(); 对拒绝的task抛出异常
  *   ThreadPoolExecutor.new DiscardPolicy();直接丢弃被拒绝的task
  *   ThreadPoolExecutor.new CallerRunsPolicy();拒绝的task直接插队执行
  *   ThreadPoolExecutor.new DiscardOldestPolicy();对拒绝的task，清除队列中等待的task,尝试为当前提交的任务腾出位置
 * **/

public class MyThreadPoolExecutor {
    private static ScheduledThreadPoolExecutor ScheduledThreadPoolExecutor;
    private static ThreadFactory DeFaultThreadFactory;
    private static RejectedExecutionHandler DefaultHandler;

    static {
        ScheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(2, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        DeFaultThreadFactory = Executors.defaultThreadFactory();
        DefaultHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
    }

    public static ThreadPoolExecutor threadPoolFactory(int corePoolSize,
                                                       int maximumPoolSize,
                                                       long keepAliveTime,
                                                       TimeUnit unit,
                                                       BlockingQueue<Runnable> workQueue,
                                                       ThreadFactory threadFactory,
                                                       RejectedExecutionHandler handler ){
        if (corePoolSize < 0 ||
                maximumPoolSize <= 0 ||
                maximumPoolSize < corePoolSize ||
                keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();

        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static void schedulePoolTest() {
        Thread thread = new Thread(() -> {
            System.out.println("执行时间："+new Date());
            System.out.println("延迟3秒执行");
        });
        System.out.println("开始时间："+new Date());
        ScheduledThreadPoolExecutor.schedule(thread,3000,TimeUnit.MILLISECONDS);
    }

    /**
      * 单线程池
      * @description:此方法一般会报错，报错原因[是否报错由DefaultHandler决定]
      * task1 running
      * task2 waiting in taskQueue
      * task2 ：因为没有可用线程,且无可用等待队列(size=1),throw Exception
      * SingleThreadExecutor: corePoolSize=1 && maximumPoolSize=1 && BlockingQueue大小无限制
    **/
    public static void singlePoolExecutor(){
        Thread thread = new Thread(()-> System.out.println("task1执行ing"));
        Thread thread1 = new Thread(()-> System.out.println("task2执行ing"));
        Thread thread2 = new Thread(()-> System.out.println("task3执行ing"));
        Thread thread3 = new Thread(()-> System.out.println("task4执行ing"));
        ThreadPoolExecutor singlePoolExecutor = threadPoolFactory(1,1,0L,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1),DeFaultThreadFactory,DefaultHandler);
        singlePoolExecutor.execute(thread);
        singlePoolExecutor.execute(thread1);
        singlePoolExecutor.execute(thread2);
        singlePoolExecutor.execute(thread3);
    }

    /**
      * @description:fixedPoolExecutor固定大小线程池
      * corePoolSize = maximumPoolSize >0 && BlockingQueue大小无限制
    **/
    public static void fixedPoolExecutor(){
        ThreadPoolExecutor fixedPoolExecutor = threadPoolFactory(5,5,0L,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(),DeFaultThreadFactory,DefaultHandler);
    }

    /**
     * @description:cachedPoolExecutor无限线程池，任何提交的task都会新建一个线程立即执行
     * corePoolSize=0 && maximumPoolSize=Integer.MAX_VALUE && BlockingQueue= new SynchronousQueue<>();
     * SynchronousQueue表示直接交给线程执行，不在队列中保存
     **/
    public static void cachedPoolExecutor(){
        ThreadPoolExecutor cachedPoolExecutor = threadPoolFactory(0,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,
                new SynchronousQueue<>(),DeFaultThreadFactory,DefaultHandler);
    }

    public static void main(String[] args) {
        try {
            System.out.println("singlePoolExecutor execute");
            singlePoolExecutor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
