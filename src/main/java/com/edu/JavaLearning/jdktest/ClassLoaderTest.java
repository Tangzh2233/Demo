package com.edu.JavaLearning.jdktest;

import java.net.URL;

/**
 * @author Tangzhihao
 * @date 2018/2/26
 */

public class ClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> forName = Class.forName("com.edu.JavaLearning.jdktest.ClassLoaderTest");
        Object instance = forName.newInstance();
        new ClassLoaderTest().getClassLoader();
        ClassLoader loader = ClassLoaderTest.class.getClassLoader();
        System.out.println("/tang".substring(1));
    }

    public ClassLoader getClassLoader(){
        ClassLoader loader = null;
        URL url = this.getClass().getResource("resource.properties");
        loader = this.getClass().getClassLoader();
        loader = ClassLoaderTest.class.getClassLoader();
        loader = ClassLoader.getSystemClassLoader();
        loader = Thread.currentThread().getContextClassLoader();
        return  loader;
    }
}
