package com.edu.JavaLearning.设计模式.适配器.类对象适配器;

/**
 * @author Tangzhihao
 * @date 2018/3/5
 * 对象适配器 适配110v 220v 输出5v (实现同一接口)Src:Adapter=N:1
 * 类对象适配器 就是继承Src类，实现Target类。缺点,Src:Adapter=1:1
 */

public class Adapter implements TargetV{
    TargetV targetV;
    Adapter(TargetV targetV){
        this.targetV = targetV;
    }

    public int outputV() {
        int src = targetV.outputV();
        int dst = 0;
        System.out.println("开始适配电压");
        if(src==110)
            dst = src-105;
        if(src==220)
            dst = src/44;
        System.out.println("适配完成，电压输出"+dst);
        return dst;
    }
}
