package com.edu.JavaLearning.jvm;


import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tangzh
 * @Date: 2019/7/4$ 2:39 PM$
 **/
public class OOMTest {


    private static OOMTest instance;

    private OOMTest instance1;


    private List<String> list = new ArrayList(){
        {
            add("1");
            add("2");
        }
    };


    public void lock(){
        synchronized (list){
            try {
                System.out.println("已锁定休眠中");
                list.add("3");
                Thread.sleep(3000);
                System.out.println("休眠结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void get() throws InterruptedException {
        System.out.println("尝试get");
        System.out.println(list.toString());
        Thread.sleep(3000);
        System.out.println("再次get"+list.toString());
    }

    public static void main(String[] args) {
        OOMTest oomTest = new OOMTest();
        new Thread(new Worker1(oomTest)).start();
        new Thread(new Worker2(oomTest)).start();
    }

}

class Worker1 implements Runnable{
    private OOMTest oomTest;

    Worker1(OOMTest oomTest){
        this.oomTest = oomTest;
    }

    public void run(){
        oomTest.lock();
    }
}

class Worker2 implements Runnable{
    private OOMTest oomTest;

    Worker2(OOMTest oomTest){
        this.oomTest = oomTest;
    }

    public void run(){
        try {
            oomTest.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
