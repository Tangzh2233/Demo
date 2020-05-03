package com.edu.JavaLearning.设计模式.状态模式;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/8 7:15 PM
 **/
public class StateD extends PayState{

    public StateD(Context context){
        super(context);
    }

    @Override
    public void doNext(int result) {
        switch (result){
            case 0:
                System.out.println("D -> D");
                this.context.setState(context.D);
                break;
            case 1:
                System.out.println("D -> A");
                this.context.setState(context.A);
                break;
            case 2:
                System.out.println("D -> B");
                this.context.setState(context.B);
                break;
            case 3:
                System.out.println("D -> C");
                this.context.setState(context.C);
                break;
        }
    }

    @Override
    public int getService() {
        return random.nextInt(3);
    }
}
