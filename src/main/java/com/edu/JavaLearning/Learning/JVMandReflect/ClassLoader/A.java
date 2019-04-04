package com.edu.JavaLearning.Learning.JVMandReflect.ClassLoader;

/**
 * @author Tangzhihao
 * @date 2018/4/25
 */

public class A {
    //方法重载
    public String show(D obj) {
        return "A&D";
    }
    public String show(A obj) {
        return "A&A";
    }
}

class B extends A{
    public String show(B obj) {
        return "B&B";
    }

    @Override
    public String show(A obj) {
        return "B&A";
    }

    public String show(){return "a2无法访问";}
}

class C extends B{

}

class D extends B{

}

class Test{
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new B();
        B b = new B();
        C c = new C();
        D d = new D();

    //  System.out.println(a2.show()); --error
        System.out.println(a1.show(b));  //1aa
        System.out.println(a1.show(c));//aa
        System.out.println(a1.show(d));  //3ad
        System.out.println(a2.show(a1)); //B中方法A参数类型ba
        System.out.println(a2.show(b));  //5ba A.show(A)-->被重写--B.show(A)-->print(B&&A)
        System.out.println(a2.show(c));  //6ba
        System.out.println(a2.show(d));  //7---ad
        System.out.println(b.show(b));//bb
        System.out.println(b.show(d));//ad
        System.out.println(b.show(c));//bb
    }
}
