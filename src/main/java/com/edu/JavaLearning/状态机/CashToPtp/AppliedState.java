package com.edu.JavaLearning.状态机.CashToPtp;


/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 下午7:16$
 **/
public class AppliedState extends CashToPtpState {


    public AppliedState(CashToPtpContext context) {
        super(context);
    }

    @Override
    public int getNextService() {
        return 1;
    }

    @Override
    public void doNext() {
        this.context.setState(context.acsncTry);
    }
}
