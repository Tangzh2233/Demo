package com.edu.JavaLearning.设计模式.代理模式and装饰模式.装饰模式;

/**
 * Created by Administrator on 2017/8/9.
 */
public class Person {
    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println(name + "穿着……");
    }
}
