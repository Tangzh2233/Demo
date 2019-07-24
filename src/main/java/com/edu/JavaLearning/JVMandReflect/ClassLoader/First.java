package com.edu.JavaLearning.JVMandReflect.ClassLoader;

/**
 * @author Tangzhihao
 * @date 2018/4/25
 * 1:First.sout()-->sout被子类重写所以print(second)
 * 2:Second.sout()-->print(second)
 */

public class First {
    public First(){
        sout();
    }
    public void sout(){
        System.out.println("First");
    }
}

class Second extends First{
    public Second(){
        sout();
    }
    @Override
    public void sout(){
        System.out.println("second");
    }

    public static void main(String[] args) {
        new Second();
    }
}
