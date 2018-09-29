package com.edu.JavaLearning.设计模式.constructpattern;

/**
 * Created by Administrator on 2017/8/15.
 */
public class ConstructTest {
    public static void main(String[] args) {
        Directer directer = new Directer();
        Builder bodya = new BuildBodyA();
        Builder bodyb = new BuildBodyB();
        System.out.println(directer.construct(bodya).toString());
        System.out.println(directer.construct(bodyb).toString());
    }

}
