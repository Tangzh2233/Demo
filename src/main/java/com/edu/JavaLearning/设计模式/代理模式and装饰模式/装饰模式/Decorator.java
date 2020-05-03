package com.edu.JavaLearning.设计模式.代理模式and装饰模式.装饰模式;

/**
 * Created by Administrator on 2017/8/
 * 装饰者,拥有要装饰的对象。
 * 二者最主要的区别是：
 * 代理模式中，代理类对被代理的对象有控制权，决定其执行或者不执行。
 * 而装饰模式中，装饰类对代理对象没有控制权，只能为其增加一层装饰，
 * 以加强被装饰对象的功能，仅此而已。
 */
public class Decorator extends Person {
    protected Person component;

    //装扮
    public void decorator(Person component) {
        this.component = component;
    }

    @Override
    public void show() {
        if (component != null) {
            component.show();
        }
    }

}
