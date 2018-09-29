package com.edu.JavaLearning.jdktest.threadLearn;


import java.util.concurrent.ExecutorService;

/**
 * @author Tangzhihao
 * @date 2017/10/17
 */

public class ThreadTest {
    private static ThreadTest threadTest;

    public static void main(String[] args) {
        final PublicOnject po = new PublicOnject();

        try {
            Thread thread1 = new Thread(){
                @Override
                public void run(){
                    po.strPrint();
                }
            };
            thread1.setName("a");
            thread1.start();
            thread1.sleep(2000);
        //    thread1.resume();

            Thread thread2 = new Thread(){
                @Override
                public void run(){
                    int count = 0;
                    long beginTime = System.currentTimeMillis();
                    for(int i=0;i<9000000;i++){
                        Thread.yield();
                        count = count + i;
                    }
                    long endTime = System.currentTimeMillis();
                    System.out.println("耗时:"+(endTime-beginTime)+"毫秒");
                }
            };
            thread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }














    /*    StopThread thread = new StopThread();

        try {
            thread.start();
            Thread.sleep(0);
            thread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("main catch");
            e.printStackTrace();
        }*/
    }
    /*public static void main(String[] args) {
        final Thread1 thread1 = new Thread1();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        long begin = System.currentTimeMillis();
        for (int i=0;i<5;i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("value is "+thread1.getValue());
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        executorService.shutdown();
        try {
            Thread.currentThread().wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isOver(executorService);

        long end = System.currentTimeMillis();
        System.out.println("耗时:"+(end-begin)+"ms");
    }*/

    public static void isOver(ExecutorService executorService){
        if (executorService.isTerminated()){
            Thread.currentThread().notifyAll();
        }else {
            isOver(executorService);
        }
    }
}
