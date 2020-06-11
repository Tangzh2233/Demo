package com.edu.JavaLearning.设计模式.创建型模式.原型模式;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/22 4:42 PM
 * 浅拷贝 + 深拷贝
 **/
public class DeepClone implements Cloneable, Serializable {

    private static final long serialVersionUID = -599923329565748709L;

    private int id;

    private String name;

    private String pwd;

    private List<String> list;

    private static transient byte[] deepCloneBytes;

    /**
     * 浅拷贝
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected DeepClone clone() throws CloneNotSupportedException {
        return (DeepClone) super.clone();
    }

    /**
     * 序列化实现深度clone
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected DeepClone doDeepClone() throws IOException, ClassNotFoundException {
        if (deepCloneBytes == null || deepCloneBytes.length <= 0) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject(this);
            deepCloneBytes = outputStream.toByteArray();
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(deepCloneBytes);
        ObjectInputStream inObjectStream = new ObjectInputStream(inputStream);
        return (DeepClone) inObjectStream.readObject();
    }

    public static void main(String[] args) throws CloneNotSupportedException, IOException, ClassNotFoundException {
        DeepClone item = new DeepClone(1, "哈哈", "1q2w3e4r", Lists.newArrayList("滴滴滴"));

        DeepClone clone = item.clone();
        DeepClone deepClone = item.doDeepClone();
        DeepClone doDeepClone = deepClone.doDeepClone();

        System.out.println(clone);
        System.out.println(deepClone);
        System.out.println(doDeepClone);

    }

    public DeepClone(int id, String name, String pwd, List<String> list) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DeepClone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", list=" + list +
                '}';
    }
}
