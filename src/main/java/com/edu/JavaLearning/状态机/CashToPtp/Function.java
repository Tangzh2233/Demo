package com.edu.JavaLearning.状态机.CashToPtp;

/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 下午7:45$
 **/
public class Function {

    public static void main(String[] args) {
        CashToPtpContext context = new CashToPtpContext();
        for (int i = 0;i<30;i++){
            System.out.println(context.getNextService());
            functionExecute(context.getNextService());
            context.doNext();

        }
    }

    public static void functionExecute(int i){
        switch (i){
            case 1:
                System.out.println("doing Route");
                break;
            case 2:
                System.out.println("doing Acsnc");
                break;
            case 3:
                System.out.println("doing Acm");
                break;
            default:
                    System.out.println("success");
                    break;
        }
    }
    public void swop(int a,int b){
        a = a+b;
        b = a-b;
        a = a-b;
    }
}
