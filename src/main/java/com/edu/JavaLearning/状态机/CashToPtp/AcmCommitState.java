package com.edu.JavaLearning.状态机.CashToPtp;


/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 下午7:18$
 **/
public class AcmCommitState extends CashToPtpState {
    public AcmCommitState(CashToPtpContext context) {
        super(context);
    }

    @Override
    public int getNextService() {
        return 3;
    }

    @Override
    public void doNext() {
        this.context.setState(context.success);
    }
}
