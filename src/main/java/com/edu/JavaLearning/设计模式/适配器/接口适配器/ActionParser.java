package com.edu.JavaLearning.设计模式.适配器.接口适配器;

/**
 * @author Tangzhihao
 * @date 2018/1/30
 */

import java.util.List;

/**
 * @author: Tangzhihao
 * @date: 2018/1/30 15:50
 * @description:适配器模式：Interface和其实现类之间多了一个AbstractClass
 */
public interface ActionParser {

    void mehtodA(String str);

    //泛型的使用
    <E> List<E> query();
}
