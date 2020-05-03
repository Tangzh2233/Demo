package com.edu.JavaLearning.设计模式.状态模式;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/8 7:24 PM
 **/
public class StateB extends PayState{


    public StateB (Context context){
        super(context);
    }

    @Override
    public void doNext(int result) {
        switch (result){
            case 0:
                System.out.println("B -> D");
                this.context.setState(context.D);
                break;
            case 1:
                System.out.println("B -> A");
                this.context.setState(context.A);
                break;
            case 2:
                System.out.println("B -> B");
                this.context.setState(context.B);
                break;
            case 3:
                System.out.println("B -> C");
                this.context.setState(context.C);
                break;
        }
    }

    @Override
    public int getService() {
        return random.nextInt(3);
    }
}
