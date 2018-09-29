package com.edu.JavaLearning.设计模式.适配器.接口适配器;

import java.util.List;

/**
 * @author: Tangzhihao
 * @date: 2018/1/30 15:54
 * @description:当不需要全部实现接口提供的方法时，
 * 可先设计一个抽象类实现接口，并为该接口中每个方法提供一个默认实现（空方法），
 * 那么该抽象类的子类可有选择地覆盖父类的某些方法来实现需求，
 * 它适用于一个接口不想使用其所有的方法的情况。
 * 抽象类可以不实现接口方法，让其继承者具体实现
 */
public abstract class AbstractActionParser implements ActionParser{

    abstract void methodB(String str1);

    public void mehtodA(String str){}

    //泛型的使用
    public <E> List<E> query(){
        return null;
    }
}
