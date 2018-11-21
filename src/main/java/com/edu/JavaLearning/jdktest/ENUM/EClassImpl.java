package com.edu.JavaLearning.jdktest.ENUM;

/**
 * @author Tangzhihao
 * @date 2018/3/1
 * 枚举类还有这种操作的？？
 * 接口用枚举类的方式实现!!!
 */

public enum EClassImpl implements InterfaceA{

    MemberA{
        @Override
        public void execute(Object o) {

        }
    },

    MemberB{
        @Override
        public void execute(Object o) {
            System.out.println("Member B");
        }
    }
}
