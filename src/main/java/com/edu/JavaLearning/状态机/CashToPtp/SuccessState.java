package com.edu.JavaLearning.状态机.CashToPtp;


/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 下午7:19$
 **/
public class SuccessState extends CashToPtpState {
    public SuccessState(CashToPtpContext context) {
        super(context);
    }

    @Override
    public int getNextService() {
        return 4;
    }

    @Override
    public void doNext() {
        this.context.setState(this.context.applied);
    }
}
