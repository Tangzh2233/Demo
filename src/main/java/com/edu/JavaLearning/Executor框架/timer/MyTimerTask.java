package com.edu.JavaLearning.Executor框架.timer;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/8/31 20:30
 * @description
 **/
public abstract class MyTimerTask implements Runnable{

    long nextExecutionTime;

    long period = 0;

    public abstract void run();

    @Override
    public String toString() {
        return "MyTimerTask{" +
                "nextExecutionTime=" + nextExecutionTime +
                ", period=" + period +
                '}';
    }
}
