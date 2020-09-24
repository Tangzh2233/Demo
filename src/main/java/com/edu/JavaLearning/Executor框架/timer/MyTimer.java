package com.edu.JavaLearning.Executor框架.timer;

import com.edu.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/8/31 20:29
 * @description Timer类仿照实现
 **/
public class MyTimer {

    private final TimerQueue timerQueue = new TimerQueue();
    private final TimerThread thread = new TimerThread(timerQueue);

    public MyTimer() {
        thread.setName("Timer Thread");
        thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        MyTimer timer = new MyTimer();
        System.out.println("启动：" + DateUtil.format(LocalDateTime.now()));
        timer.schedule(new MyTimerTask() {
            @Override
            public void run() {
                System.out.println("task1:" + DateUtil.format(LocalDateTime.now()));
            }
        }, 2000, 3000);

        timer.schedule(new MyTimerTask() {
            @Override
            public void run() {
                System.out.println("task2:" + DateUtil.format(LocalDateTime.now()));
            }
        }, 2000, 3000);

        timer.schedule(new MyTimerTask() {
            @Override
            public void run() {
                System.out.println("task3:" + DateUtil.format(LocalDateTime.now()));
            }
        }, 3000, 0);

        Thread.sleep(10000);
        System.out.println("timer cancel");
        timer.cancel();
    }

    public void schedule(MyTimerTask task, long delay, long period) {
        long timeMillis = System.currentTimeMillis();
        if (delay > 0) {
            task.nextExecutionTime = timeMillis + delay;
        }
        task.period = period;
        timerQueue.add(task);
        synchronized (thread.lock){
            if (task.nextExecutionTime <= timerQueue.getMin().nextExecutionTime) {
                thread.lock.notifyAll();
            }
        }
    }

    public void cancel(){
        thread.newTaskCanBeSchedule = false;
        timerQueue.clear();
    }
}

/**
 * Timer执行线程,循环遍历taskQueue
 */
class TimerThread extends Thread {

    //线程休眠lock
    final Object lock = new Object();

    /**
     * Timer是否可用标识
     */
    boolean newTaskCanBeSchedule = true;

    final TimerQueue queue;

    public TimerThread(TimerQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            mainLoop();
        } finally {
            newTaskCanBeSchedule = false;
            queue.clear();
        }
    }

    private void mainLoop() {
        while (true) {
            MyTimerTask task;
            try {
                synchronized (lock){
                    boolean run = false;
                    while (queue.isEmpty() && newTaskCanBeSchedule) {
                        lock.wait();
                    }
                    if(queue.isEmpty()){
                        break;
                    }
                    task = queue.getMin();
                    long currentTime = System.currentTimeMillis();
                    long nextTime = task.nextExecutionTime;
                    if (currentTime >= nextTime) {
                        run = true;
                        if (task.period == 0) {
                            queue.removeMin();
                        } else {
                            queue.rescheduleMin(task.period > 0 ? currentTime + task.period : currentTime - task.period);
                        }
                    }
                    if (!run) {
                        lock.wait(nextTime - currentTime);
                    }
                    if (run) {
                        task.run();
                    }
                }
            } catch (Exception e) {
                //Exception 任务异常不会退出
            }
        }
    }
}

/**
 * TimerTask的有序队列,数据结构为小顶堆
 * 保证线程安全
 */
class TimerQueue {

    private int size = 0;

    //小顶堆,nextExecutionTime最小的在第一位,parent=n leftChild=2n rightChild=2n+1
    private MyTimerTask[] queue = new MyTimerTask[128];

    /**
     * 从1开始
     *
     * @param task
     */
    public synchronized void add(MyTimerTask task) {
        if(size + 1 == queue.length){
            queue = Arrays.copyOf(queue,2 * queue.length);
        }
        queue[++size] = task;
        fixUp(size);
    }

    public synchronized MyTimerTask getMin() {
        return queue[1];
    }

    public synchronized MyTimerTask get(int i) {
        return queue[i];
    }

    public synchronized void removeMin() {
        queue[1] = queue[size];
        queue[size--] = null;
        fixDown(1);
    }

    public synchronized void rescheduleMin(long nextTime) {
        queue[1].nextExecutionTime = nextTime;
        //此时小顶堆被破坏，需要fix
        fixDown(1);
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized void clear() {
        for (int i = 1; i <= queue.length - 1; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    /**
     * 构建小顶堆,保证父节点最小。自下而上
     * @param k
     */
    private void fixUp(int k) {
        while (k > 1){
            // k 的父节点 j
            int j = k >> 1;
            // j <= k 则小顶堆依旧成立，什么也不干
            if(queue[j].nextExecutionTime <= queue[k].nextExecutionTime){
                break;
            }
            swap(k,j);
            k = j;
        }
    }

    /**
     * 小顶堆修复，自上而下
     *
     * @param index
     */
    private void fixDown(int index) {
        int k;
        while ((k = index << 1) <= size) {
            // 右孩子最小
            if (k < size && queue[k].nextExecutionTime >= queue[k + 1].nextExecutionTime) {
                k++;
            }
            // 父节点比最小的孩子小，满足小顶堆
            if (queue[index].nextExecutionTime <= queue[k].nextExecutionTime) {
                break;
            }
            //否则交换
            swap(k, index);
            index = k;
        }
    }

    private void swap(int from, int to) {
        MyTimerTask temp = queue[to];
        queue[to] = queue[from];
        queue[from] = temp;
    }

    @Override
    public String toString() {
        return "TimerQueue{" +
                "size=" + size +
                ", queue=" + Arrays.toString(queue) +
                '}';
    }
}