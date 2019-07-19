package com.edu.JavaLearning.设计模式.Sington;

/**
 * @author Tangzhihao
 * @date 2018/4/24
 */

public class SingleTest {

    static {
        System.out.println("1");
    }

    public static void main(String[] args) {
        staticFunction();
    }

    static SingleTest st = new SingleTest();

    {
        System.out.println("2");
    }

    SingleTest() {
        System.out.println("3");
        System.out.println("a=" + a + ",b=" + b);
    }

    public static void staticFunction() {
        System.out.println("4");
        System.out.println(b);
    }

    int a = 110;
    static int b = 112;
}
