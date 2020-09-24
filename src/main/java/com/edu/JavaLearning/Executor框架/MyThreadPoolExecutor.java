package com.edu.JavaLearning.Executor框架;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tangzh
 * @Date: 2018/10/12$ 下午3:07$
 * /**
 *       * corePoolSize:核心线程数
 *       * maximumPoolSize:最大线程数
 *       * keepAlivetime:非核心线程,闲置时长
 *       * TimeUnit:闲置时长的单位
 *       * BlockingQueue<>:Task的等待队列
 *       * ThreadFactory: 创建线程的方式
 *       * RejectedExecutionHandler:
  *       当一个任务被添加进线程池时，执行策略：
  *      * 1.线程数量未达到corePoolSize，则新建一个线程(核心线程)执行任务
  *      * 2.线程数量达到了corePools，则将任务移入队列等待
  *      * 3.队列已满，新建线程(非核心线程)执行任务
  *      * 4.队列已满，总线程数又达到了maximumPoolSize，就会由(RejectedExecutionHandler)抛出异常
  *
  *   常用的BlockingQueue
  *     SynchronousQueue:这个队列接收到任务的时候，会直接提交给线程处理。
  *     LinkedBlockingQueue:链表队列,无限大。会导致maximumPoolSize失效。
 *                          此队列按 FIFO（先进先出）排序元素。
 *                          队列的头部 是在队列中时间最长的元素。
 *                          队列的尾部 是在队列中时间最短的元素。
 *                          新元素插入到队列的尾部，并且队列获取操作会获得位于队列头部的元素。
 *                          链接队列的吞吐量通常要高于基于数组的队列，但是在大多数并发应用程序中，其可预知的性能要低。
  *     ArrayBlockingQueue: 数组支持的有界阻塞队列,固定大小。
 *                          通过将公平性 (fairness) 设置为 true 而构造的队列允许按照 FIFO 顺序访问线程。
 *                          公平性通常会降低吞吐量，但也减少了可变性和避免了“不平衡性”。
 *      DelayQueue:         延迟执行。
 *                          Delayed 元素的一个无界阻塞队列，只有在延迟期满时才能从中提取元素。
  *   异常策略，
  *   ThreadPoolExecutor.new AbortPolicy(); 对拒绝的task抛出异常
  *   ThreadPoolExecutor.new DiscardPolicy();直接丢弃被拒绝的task
  *   ThreadPoolExecutor.new CallerRunsPolicy();拒绝的task直接插队执行
  *   ThreadPoolExecutor.new DiscardOldestPolicy();对拒绝的task，清除队列中等待的task,尝试为当前提交的任务腾出位置
 * **/

public class MyThreadPoolExecutor {
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private static ThreadFactory DeFaultThreadFactory;
    private static RejectedExecutionHandler DefaultHandler;
    private static SynchronousQueue synchronousQueue;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");



    static {
        scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        DeFaultThreadFactory = new DefaultThreadFactory("myThread",true,5);
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
        scheduledThreadPoolExecutor.schedule(thread,3000,TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
//        scheduleAtFixDelay();
//        scheduleAtFixRate();
//        schedule();
        System.out.println(System.currentTimeMillis());
    }

    /**
     * @see ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)
     * 当任务耗时大于period时间时,上一个任务结束,后一个任务立即执行
     * 不会像scheduleWithFixedDelay会再等待delay的时间
     */
    public static void scheduleAtFixRate(){
        LocalDateTime now = LocalDateTime.now();
        System.out.println("启动：" + now.format(formatter));
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(LocalDateTime.now().format(formatter));
//                try {
//                    Thread.sleep(6000);
//                } catch (InterruptedException e) {
//                }
            }
        },2000,2000,TimeUnit.MILLISECONDS);
    }

    /**
     * @see ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)
     * 3s后启动,任务执行了6s+delay5s,所以下次执行是11s后。
     * 即 总是在之前的任务执行结束后的delay时间后执行
     */
    public static void scheduleAtFixDelay(){
        LocalDateTime now = LocalDateTime.now();
        System.out.println("启动：" + now.format(formatter));
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println(LocalDateTime.now().format(formatter));
                throw new IllegalArgumentException("参数异常");
            }
        },1000,2000,TimeUnit.MILLISECONDS);
    }

    public static void schedule(){
        LocalDateTime now = LocalDateTime.now();
        System.out.println("启动：" + now.format(formatter));
        scheduledThreadPoolExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(LocalDateTime.now().format(formatter));
            }
        },2000,TimeUnit.MILLISECONDS);
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
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Task1 执行ing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Task2 执行ing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Task3 执行ing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread3 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Task4 执行ing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ThreadPoolExecutor singlePoolExecutor = threadPoolFactory(1,1,0L,TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1),DeFaultThreadFactory,DefaultHandler);
        singlePoolExecutor.execute(thread);
        singlePoolExecutor.submit(thread1);
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

}

/**
 * 自定义ThreadFactory
 */
class DefaultThreadFactory implements ThreadFactory {

    private final ThreadGroup threadGroup;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private String namePrefix;
    private final int priority;
    private final boolean daemon;


    DefaultThreadFactory(String namePrefix,boolean daemon,int priority){
        SecurityManager manager = System.getSecurityManager();
        this.threadGroup = (manager != null) ? manager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
        this.priority = priority;
        this.daemon = daemon;
    }


    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, namePrefix + threadNumber.getAndIncrement());
        thread.setDaemon(daemon);
        thread.setPriority(priority);
        thread.setContextClassLoader(getClass().getClassLoader());
        return thread;
    }
}
