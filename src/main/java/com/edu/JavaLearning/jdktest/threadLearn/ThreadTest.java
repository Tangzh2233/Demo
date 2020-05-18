package com.edu.JavaLearning.jdktest.threadLearn;


import java.util.concurrent.ExecutorService;

/**
 * @author Tangzhihao
 * @date 2017/10/17
 */

public class ThreadTest {
    private static ThreadTest threadTest;

    public static void main(String[] args) throws InterruptedException {

        Thread thread3 = new Thread(new TestTask(1));
        Thread thread4 = new Thread(new TestTask(2));

        thread3.start();
        thread4.start();
        /**
         * https://www.jianshu.com/p/fc51be7e5bc0
         * join的实质是wait方法,将当前执行线程(⚠️非Thread.join()的这个Thread)wait阻塞在当前class对象上。
         * 区别在于。Thread.join() 在Thread线程执行完成以后,会自动调用notifyAll方法,唤醒调用Thread.join()方法的线程
         * main线程把自己wait在ThreadTest.class上
         * 只有Thread3,执行完成以后,线程退出会执行lock.notify唤醒主线程
         */
        thread3.join();
        System.out.println("=======Thread3 join 解除======");
        /**
         * 到这一步时Thread3已经运行完毕,因为都设置的1s,此时Thread3也执行完毕了
         * 对应
         * @see Thread#join(long) 中的 isAlive()为false就不再wait
         */
        thread4.join();



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
            Thread.sleep(2000);
        //    thread1.resume();

            Thread thread2 = new Thread(){
                @Override
                public void run(){
                    int count = 0;
                    long beginTime = System.currentTimeMillis();
                    for(int i=0;i<900;i++){
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

    static class TestTask implements Runnable {

        private int seq;

        public TestTask(int seq) {
            this.seq = seq;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(seq + "哈哈哈");
        }
    }
}
