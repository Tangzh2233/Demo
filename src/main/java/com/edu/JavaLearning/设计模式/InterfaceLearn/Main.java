package com.edu.JavaLearning.设计模式.InterfaceLearn;

/**
 * @Author: tangzh
 * @Date: 2018/11/1$ 上午10:10$
 **/
public class Main{

    public static void main(String[] args) {
        Interface a = new MainA();
        Interface b = new MainB();
    }

}
class MainA extends AbstractA{

    @Override
    public void methodA1() {

    }

    @Override
    public void methodA2() {

    }
}
class MainB extends AbstractB{

    @Override
    public void methodB1() {

    }

    @Override
    public void methodB2() {

    }
}
