package com.edu.JavaLearning.Learning.JVM.ClassLoader;

import java.io.*;

/**
 * @author Tangzhihao
 * @date 2018/4/26
 * 自定义classLoader
 */

public class MyClassLoader extends ClassLoader{

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if(classData==null){
            throw new ClassNotFoundException();
        }else{
            return defineClass(name,classData,0,classData.length);
        }
    }

    private byte[] loadClassData(String name){
        String fileName = name+".class";

        try {
            byte[] classData = new byte[1024];
            FileInputStream in = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(in);
            DataInputStream dis = new DataInputStream(in);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int length = 0;
            while ((length = in.read(classData))!=-1){
                bos.write(classData,0,length);
            }
            return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}
