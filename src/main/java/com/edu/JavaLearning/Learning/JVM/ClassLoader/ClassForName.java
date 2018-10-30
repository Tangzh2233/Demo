package com.edu.JavaLearning.Learning.JVM.ClassLoader;

/**
 * @author Tangzhihao
 * @date 2018/4/26
 *1.命令行启动应用时候由JVM初始化加载
 *2.通过Class.forName()方法动态加载
 *3.通过ClassLoader.loadClass()方法动态加载
 * Class.forName()：将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块。
 * ClassLoader.loadClass()：只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance才会去执行static块。
 * 注：Class.forName(name, initialize, loader)带参函数也可控制是否加载static块。
 * 并且只有调用了newInstance()方法采用调用构造函数，创建类的对象 。
 *
 * 注意：因为一个类中static只加载一次，所以本例的测试顺序需要注意。否则得不到想要的结果
 */

public class ClassForName {
    static {
        System.out.println("加载静态块!");
    }
    ClassForName(){
        System.out.println("加载构造方法!");
    }
}

class LoaderTest{
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        System.out.println(loader);
        Class.forName("com.edu.JavaLearning.Learning.JVM.ClassLoader.ClassForName");
        System.out.println("===========");
        loader.loadClass("com.edu.JavaLearning.Learning.JVM.ClassLoader.ClassForName").newInstance();
        System.out.println("===========");
        Class.forName("com.edu.JavaLearning.Learning.JVM.ClassLoader.ClassForName",false,loader);
    }
}
