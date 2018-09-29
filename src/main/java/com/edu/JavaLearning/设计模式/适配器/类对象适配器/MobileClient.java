package com.edu.JavaLearning.设计模式.适配器.类对象适配器;

/**
 * @author Tangzhihao
 * @date 2018/3/5
 * 需要5v电压的client类
 */

public class MobileClient {

    public static void main(String[] args) {
        MobileClient client = new MobileClient();
        client.charing(new Adapter(new Src220V()));
        System.out.println();
        client.charing(new Adapter(new Src110V()));
        System.out.println();
        client.charing(new Adapter(new Src300V()));
    }

    //充电类
    public void charing(TargetV targetV){
        int result = targetV.outputV();
        if(result==5){
            System.out.println("输出"+result+"V 适配成功!");
            System.out.println("正常充电");
        }else if(result>5){
            System.out.println("输出"+result+"V 适配失败!");
            System.out.println("boon 瞎卡拉卡");
        }else {
            System.out.println("输出"+result+"V 适配失败!");
            System.out.println("电压过低!!");
        }
    }
}
