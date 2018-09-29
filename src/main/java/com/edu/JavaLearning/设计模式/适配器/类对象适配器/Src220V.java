package com.edu.JavaLearning.设计模式.适配器.类对象适配器;

/**
 * @author Tangzhihao
 * @date 2018/3/5
 * 输出220v电压的资源类
 */

public class Src220V implements TargetV{

    private int src = 220;
    @Override
    public int outputV() {
        System.out.println("我是"+src+"V");
        return src;
    }
}
