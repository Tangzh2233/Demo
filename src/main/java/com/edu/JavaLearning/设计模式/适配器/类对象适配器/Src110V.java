package com.edu.JavaLearning.设计模式.适配器.类对象适配器;

/**
 * @author Tangzhihao
 * @date 2018/3/5
 * 输出110v电压的资源类
 */

public class Src110V implements TargetV{

    private int src = 110;
    @Override
    public int outputV() {
        System.out.println("我是"+src+"V");
        return src;
    }
}
