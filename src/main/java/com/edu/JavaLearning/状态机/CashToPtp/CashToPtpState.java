package com.edu.JavaLearning.状态机.CashToPtp;

import com.edu.JavaLearning.状态机.IPayState;

/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 下午7:33$
 **/
public abstract class CashToPtpState implements IPayState {

    public CashToPtpContext context;

    protected CashToPtpState(CashToPtpContext context){
        this.context = context;
    }
}
