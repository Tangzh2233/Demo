package com.edu.JavaLearning.JVMandReflect.ClassLoader;

import com.alibaba.fastjson.JSONObject;
import javassist.ClassPool;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/11/26 4:10 PM
 * java 探针技术
 **/
public class JavaAgent {

}

class MyTransformer implements ClassFileTransformer{

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        return new byte[0];
    }
}
