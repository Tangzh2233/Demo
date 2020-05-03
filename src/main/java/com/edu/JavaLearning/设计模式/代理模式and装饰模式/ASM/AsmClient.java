package com.edu.JavaLearning.设计模式.代理模式and装饰模式.ASM;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/26 9:57 AM
 * ASM 字节码操作框架初探
 **/
public class AsmClient {

    public static void main(String[] args) throws IOException {
        ClassReader asmClass = new ClassReader("com.edu.JavaLearning.设计模式.代理模式and装饰模式.ASM.ASMClass");
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter classAdapter = new PrintMethodHandler(cw);
        asmClass.accept(classAdapter, ClassReader.SKIP_DEBUG);
        byte[] data = cw.toByteArray();
        File file = new File("/Users/tangzh/git-space/Demo/target/classes/com/edu/JavaLearning/设计模式/代理模式and装饰模式/ASM/ASMClass.class");
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(data);
        fout.close();

        ASMClass instance = new ASMClass();
        instance.print();
    }

}


class ASMClass {
    public void print() {
        System.out.println("Do Service ....");
    }
}

class PrintMethodHandler extends ClassAdapter {

    public PrintMethodHandler(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        MethodVisitor wrappedMv = mv;
        if (mv != null) {
            if ("print".equals(name)) {
                wrappedMv = new PreMethodAdapter(mv);
            }
        }
        return wrappedMv;
    }
}

class PreMethodAdapter extends MethodAdapter {

    public PreMethodAdapter(MethodVisitor mv) {
        super(mv);
    }

    @Override
    public void visitCode() {
        visitMethodInsn(Opcodes.INVOKESTATIC,"ASMInterceptor","preMethod","()V");
    }
}
