package com.edu.JavaLearning.设计模式.代理模式and装饰模式.代理模式;

/**
 * Created by Administrator on 2017/8/11.
 */
public interface Subject {
    String sendFlower();
    void sendDolls();
    void sendChocolate();
}
interface SubjectB{
    void get(String a,Subject subject);
}

class RealSubject implements Subject,SubjectB{

    @Override
    public String sendFlower() {
        System.out.println("送兰花");
        return "兰花";
    }

    @Override
    public void sendDolls() {
        System.out.println("送娃娃");
    }

    @Override
    public void sendChocolate() {
        System.out.println("送巧克力");
    }

    @Override
    public void get(String a,Subject subject) {
        System.out.println("data == A");
    }
}