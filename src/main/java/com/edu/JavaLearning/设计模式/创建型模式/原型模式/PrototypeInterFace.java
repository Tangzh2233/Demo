package com.edu.JavaLearning.设计模式.创建型模式.原型模式;

import lombok.Data;

import java.io.*;
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
 * 浅拷贝只拷贝基本数据类型
 * 属性是对象的只做引用拷贝.所以当一个clone对象对属性对象修改时,其他clone也会感知这个修改
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
class PrototypeInstanceA implements PrototypeInterFace,Serializable {

    public static String key = PrototypeInstanceA.class.getName();

    private String id;

    private PrototypeInstanceB prototypeInterFace;

    private static transient byte[] objectInput;

    private static transient volatile boolean isInit = false;

    static {
        //加入管理器
        PrototypeManager.manager.put(key, new PrototypeInstanceA());
    }

    private PrototypeInstanceA() {
    }

    public PrototypeInstanceA(String id,PrototypeInstanceB prototypeInterFace) {
        this.id = id;
        this.prototypeInterFace = prototypeInterFace;
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

    /**
     * 深clone实现,file对象文件
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public PrototypeInterFace deepClone() throws IOException, ClassNotFoundException {
        FileOutputStream outputStream = new FileOutputStream("/data/" + this.getClass().getSimpleName());
        ObjectOutputStream stream = new ObjectOutputStream(outputStream);
        stream.writeObject(this);

        FileInputStream inputStream = new FileInputStream("/data/" + this.getClass().getSimpleName());
        ObjectInputStream stream1 = new ObjectInputStream(inputStream);
        return (PrototypeInterFace)stream1.readObject();
    }

    /**
     * 深clone实现,byte数组
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public PrototypeInterFace deepClone2() throws IOException, ClassNotFoundException {
        if(!isInit){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject(this);
            objectInput = outputStream.toByteArray();
            isInit = true;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(objectInput);
        ObjectInputStream stream1 = new ObjectInputStream(inputStream);
        return (PrototypeInterFace)stream1.readObject();
    }

    public PrototypeInstanceB getPrototypeInterFace() {
        return prototypeInterFace;
    }

    public void setPrototypeInterFace(PrototypeInstanceB prototypeInterFace) {
        this.prototypeInterFace = prototypeInterFace;
    }
}

@Data
class PrototypeInstanceB implements PrototypeInterFace,Serializable{

    public static String key = PrototypeInstanceB.class.getName();

    private String name;

    private PrototypeInstanceB prototypeInterFace;

    static {
        //加入管理器
        PrototypeManager.manager.put(key, new PrototypeInstanceB());
    }

    private PrototypeInstanceB() {
    }

    public PrototypeInstanceB(String name) {
        this.name = name;
    }

    public PrototypeInstanceB getPrototypeInterFace() {
        return prototypeInterFace;
    }

    public void setPrototypeInterFace(PrototypeInstanceB prototypeInterFace) {
        this.prototypeInterFace = prototypeInterFace;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        PrototypeInstanceB instanceB = new PrototypeInstanceB("原型");
        PrototypeInstanceA prototype = new PrototypeInstanceA("1号",instanceB);

        PrototypeInstanceA clone = (PrototypeInstanceA)prototype.deepClone2();
        PrototypeInstanceA clone2 = (PrototypeInstanceA)prototype.deepClone2();
        prototype.getPrototypeInterFace().disPlay();
        clone.getPrototypeInterFace().setName("哈哈哈");
        clone2.getPrototypeInterFace().setName("啧啧啧");
        prototype.getPrototypeInterFace().disPlay();
        clone.disPlay();

        PrototypeInterFace cloneInstance = PrototypeManager.getCloneInstance(PrototypeInstanceA.key);
        PrototypeInterFace cloneInstance2 = PrototypeManager.getCloneInstance(PrototypeInstanceB.key);

        prototype.disPlay();
        instanceB.disPlay();
        cloneInstance.disPlay();
        cloneInstance2.disPlay();

    }
}
