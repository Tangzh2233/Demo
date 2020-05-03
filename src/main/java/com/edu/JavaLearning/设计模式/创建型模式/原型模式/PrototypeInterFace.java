package com.edu.JavaLearning.设计模式.创建型模式.原型模式;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/30 1:32 PM
 * 原型模式：用一个已经创建的实例作为原型，通过复制该原型对象来创建一个和原型相同或相似的新对象。
 *         实现Cloneable接口
 *
 * 原型模式包含以下主要角色。
 * 抽象原型类：规定了具体原型对象必须实现的接口。
 * 具体原型类：实现抽象原型类的 clone() 方法，它是可被复制的对象。
 * 访问类：使用具体原型类中的 clone() 方法来复制新的对象。
 *
 * 浅clone及深clone
 * https://blog.csdn.net/qpzkobe/article/details/81007463
 **/
public interface PrototypeInterFace extends Cloneable{
    void disPlay();
    Object clone();
}

/**
 * 原型模式可扩展为带原型管理器的原型模式，
 * 它在原型模式的基础上增加了一个原型管理器 PrototypeManager 类
 */
abstract class PrototypeManager{
    protected static Map<String, PrototypeInterFace> manager = new HashMap<>();


    public static PrototypeInterFace getCloneInstance(String name) {
        PrototypeInterFace interFace = manager.get(name);
        return (PrototypeInterFace)interFace.clone();
    }
}

@Data
class PrototypeInstanceA implements PrototypeInterFace {

    public static String key = PrototypeInstanceA.class.getName();

    private String id;

    static {
        //加入管理器
        PrototypeManager.manager.put(key, new PrototypeInstanceA());
    }

    private PrototypeInstanceA() {
    }

    public PrototypeInstanceA(String id) {
        this.id = id;
    }

    @Override
    public void disPlay() {
        System.out.println(id);
    }

    public PrototypeInterFace clone() {
        try {
            return (PrototypeInterFace)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}

@Data
class PrototypeInstanceB implements PrototypeInterFace{

    public static String key = PrototypeInstanceB.class.getName();

    private String name;

    static {
        //加入管理器
        PrototypeManager.manager.put(key, new PrototypeInstanceB());
    }

    private PrototypeInstanceB() {
    }

    public PrototypeInstanceB(String name) {
        this.name = name;
    }

    @Override
    public void disPlay() {
        System.out.println(name);
    }

    @Override
    public PrototypeInterFace clone() {
        try {
            return (PrototypeInterFace)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class main {

    public static void main(String[] args) {
        PrototypeInstanceA prototype = new PrototypeInstanceA("1号");
        PrototypeInstanceB instanceB = new PrototypeInstanceB("原型");

        PrototypeInterFace clone = prototype.clone();
        clone.disPlay();

        PrototypeInterFace cloneInstance = PrototypeManager.getCloneInstance(PrototypeInstanceA.key);
        PrototypeInterFace cloneInstance2 = PrototypeManager.getCloneInstance(PrototypeInstanceB.key);

        prototype.disPlay();
        instanceB.disPlay();
        cloneInstance.disPlay();
        cloneInstance2.disPlay();

    }
}
