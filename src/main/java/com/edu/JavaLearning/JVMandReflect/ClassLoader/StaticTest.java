package com.edu.JavaLearning.JVMandReflect.ClassLoader;

/**
 * @author Tangzhihao
 * @date 2018/4/26
 * jvm准备阶段--st=null b=0
 * jvm类构建阶段，先对静态属性赋值，再执行构造方法
 * <clinit>执行st赋值(本例的st赋值要进行对象的初始化，对象的初始化是先初始化成员变量再执行构造方法)
 * a=110->print(2)->print(3)->print(a=110,b=0[还未被赋值])----st赋值完成
 * 执行print(1)
 * 执行b赋值-----b=112
 * 执行staticFunction()->print(4)->print(b=112)
 */

public class StaticTest {
    public static void main(String[] args)
    {
        staticFunction();
    }

    static StaticTest st = new StaticTest();

    static
    {
    //    b = 99;
        System.out.println("1");
    }

    {
        System.out.println("2");
    }

    StaticTest()
    {
        System.out.println("3");
        System.out.println("a="+a+",b="+b);
    }

    public static void staticFunction(){
        System.out.println("4");
        System.out.println(b);
    }

    int a=110;
    static int b =112;
}
