package com.edu.JavaLearning.状态机.CashToPtp;

import com.edu.JavaLearning.状态机.IPayContext;
import com.edu.JavaLearning.状态机.IPayState;

/**
 * @Author: tangzh
 * @Date: 2018/9/18$ 下午7:15$
 **/
public class CashToPtpContext implements IPayContext {

    IPayState applied = new AppliedState(this);
    IPayState acsncTry = new AcsncTryState(this);
    IPayState acmCommit = new AcmCommitState(this);
    IPayState success = new SuccessState(this);

    private IPayState state = applied;
    private IPayState lastState = applied;

    @Override
    public void doNext() {
        lastState = state;
        this.state.doNext();
    }

    @Override
    public void doLast() {

    }

    @Override
    public void setState(IPayState state) {
        this.state = state;
    }

    @Override
    public int getNextService() {
        return this.state.getNextService();
    }
}
