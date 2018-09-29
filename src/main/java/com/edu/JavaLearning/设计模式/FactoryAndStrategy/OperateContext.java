package com.edu.JavaLearning.设计模式.FactoryAndStrategy;

/**
 * Created by Administrator on 2017/8/9.
 */
public class OperateContext {
    private Operate operate;
    public OperateContext(String ope){
        switch (ope){
            case "-":
                operate = new OperateSub();
                break;
            case "*":
                operate = new OperateMul();
                break;
            default:
                operate = null;
        }
    }
    public double getResult(double a,double b){
        double result = 0;
        operate.setNumber_a(a);
        operate.setNumber_b(b);
        result = operate.getResult();

        return  result;
    }
}
