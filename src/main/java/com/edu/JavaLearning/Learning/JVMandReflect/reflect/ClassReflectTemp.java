package com.edu.JavaLearning.Learning.JVMandReflect.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: tangzh
 * @Date: 2018/10/18$ 上午10:09$
 **/
public class ClassReflectTemp {

    public static void main(String[] args) {
        try {
            Class<?> aClass = Class.forName("com.edu.JavaLearning.Learning.JVMandReflect.ClassLoader.Temp");
            //===========获取所有构造方法,并打印每个构造方法的参数================//
            Constructor<?>[] constructors = aClass.getDeclaredConstructors();
            System.out.println("类"+aClass.getName()+"的构造方法有:");
            for (Constructor constructor:constructors){
                System.out.println(constructor.toGenericString());
            }
            System.out.println("================================");
            //=================获取Class的所有属性================//
            Field[] fields = aClass.getDeclaredFields();
            System.out.println("类"+aClass.getName()+"的属性有:");
            for (Field field:fields){
                System.out.println(field.toGenericString());
            }
            System.out.println("=================================");
            //=================获取Class的所有方法=================//
            Method[] methods = aClass.getDeclaredMethods();
            System.out.println("类"+aClass.getName()+"的方法有:");
            for (Method method:methods){
                System.out.println(method.toGenericString());
            }
            //获取特定的构造方法(依旧构造方法的参数创建)
            Constructor<?> constructor = aClass.getDeclaredConstructor(int.class, String.class);
            //忽略修饰符，暴力使用
            constructor.setAccessible(true);
            Temp instance = (Temp) constructor.newInstance(1, "TestData");
            //此时的instance可以调用Temp的public方法，如果想调用private 方法
            System.out.println("============调用private方法==========");
            //step:1 获取具体方法实例,("方法名","方法入参类型")
            Method privateMethodTest = aClass.getDeclaredMethod("write", String.class,String.class);
            //忽略私有修饰符，否则调用失败
            privateMethodTest.setAccessible(true);
            //（"Temp实例","方法的实际入参"）
            privateMethodTest.invoke(instance,"private Method Test","tangzhihao");
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}

class Temp{
    public int id;
    public String desc;
    private String type;

    public Temp(){
        System.out.println("公共无参数构造方法");
    }
    public Temp(int id,String desc,String type){
        this.id = id;
        this.desc = desc;
        this.type = type;
        System.out.println("公共有参构造方法"+id+","+desc+","+type);
    }

    private Temp(int id,String type){
        this.id = id;
        this.type = type;
        System.out.println("私有有参构造方法:"+id+","+type);
    }
    public void print(){
        System.out.println("我是公共无参方法print");
    }
    public void print(String name){
        System.out.println("我是公共有参方法print:"+name);
    }
    private String write(){
        System.out.println("我是私有无参方法write");
        return "true";
    }
    private String write(String type,String name){
        System.out.println("我是私有有参方法write:"+type+name);
        return type;
    }
}
