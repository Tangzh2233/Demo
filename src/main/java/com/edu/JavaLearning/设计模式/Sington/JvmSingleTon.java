package com.edu.JavaLearning.设计模式.Sington;

/**
 * @author Tangzhihao
 * @date 2018/4/26
 */

public class JvmSingleTon {
    private static JvmSingleTon instance = new JvmSingleTon();
    public static int a;
    public static int b = 8;
    private JvmSingleTon(){
        a++;
        b++;
    }
    public static JvmSingleTon getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        JvmSingleTon instance = JvmSingleTon.getInstance();
        System.out.println(JvmSingleTon.a);
        System.out.println(JvmSingleTon.b);
    }
}
