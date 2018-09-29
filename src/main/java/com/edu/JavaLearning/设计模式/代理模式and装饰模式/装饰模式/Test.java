package com.edu.JavaLearning.设计模式.代理模式and装饰模式.装饰模式;

/**
 * Created by Administrator on 2017/8/9.
 */
public class Test {
    public static void main(String[] args) {
        Person person = new Person("螳tang");
        Hat hat = new Hat();
        BigShort bigShort = new BigShort();
        TShirts tShirts = new TShirts();

        hat.decorator(person);
        bigShort.decorator(hat);
        tShirts.decorator(bigShort);
        tShirts.show();
    }
}
