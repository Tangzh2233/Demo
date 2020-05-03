package com.edu.JavaLearning.设计模式.状态模式;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/8 7:25 PM
 **/
public class StateC extends PayState{

    public StateC (Context context){
        super(context);
    }

    @Override
    public void doNext(int result) {
        switch (result){
            case 0:
                System.out.println("C -> D");
                this.context.setState(context.D);
                break;
            case 1:
                System.out.println("C -> A");
                this.context.setState(context.A);
                break;
            case 2:
                System.out.println("C -> B");
                this.context.setState(context.B);
                break;
            case 3:
                System.out.println("C -> C");
                this.context.setState(context.C);
                break;
        }
    }

    @Override
    public int getService() {
        return random.nextInt(3);
    }
}
