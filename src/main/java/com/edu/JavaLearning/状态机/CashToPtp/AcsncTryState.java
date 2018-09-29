package com.edu.JavaLearning.状态机.CashToPtp;

import com.edu.JavaLearning.状态机.IPayState;

/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 下午7:17$
 **/
public class AcsncTryState extends CashToPtpState {
    public AcsncTryState(CashToPtpContext context) {
        super(context);
    }

    @Override
    public int getNextService() {
        return 2;
    }

    @Override
    public void doNext() {
        this.context.setState(context.acmCommit);
    }
}
