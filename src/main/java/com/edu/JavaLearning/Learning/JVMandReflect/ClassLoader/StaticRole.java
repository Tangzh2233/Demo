package com.edu.JavaLearning.Learning.JVMandReflect.ClassLoader;

/**
 * @author Tangzhihao
 * @date 2018/4/26
 * 对于静态字段，只有直接定义这个字段的类才会被初始化，
 * 因此通过其子类来引用父类中定义的静态字段，
 * 只会触发父类的初始化而不会触发子类的初始化。
 */

public class StaticRole {
    static {
        System.out.println("pre one");
    }
    StaticRole(){
        System.out.println("one");
    }
}

class a extends StaticRole{
    static {
        System.out.println("pre two");
    }
    public static int num = 99;
    a(){
        System.out.println("two");
    }
}

class b extends a{
    b(){
        System.out.println("Three");
    }

    public static void main(String[] args) {
        System.out.println(num);
    }
}

/**
 * @author: Tangzhihao
 * @date: 2018/5/22 10:58
 * @description: final static 的字段。编译期之间把c.num替换成100。
 * 不会触发c的初始化。具体可见target中的d.java文件
 */
class c{
    static {
        System.out.println("c初始化!");
    }
    public final static int num = 100;
}
class d{
    public static void main(String[] args) {
        System.out.println(c.num);
    }
}