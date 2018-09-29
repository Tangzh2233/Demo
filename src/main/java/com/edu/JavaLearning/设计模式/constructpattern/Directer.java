package com.edu.JavaLearning.设计模式.constructpattern;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */
public class Directer {
    //建造者模式中中的指挥者
    public Body body = null;
    public List<String> construct(Builder builder){
        body = new Body();
        body.body.add(builder.createArm());
        body.body.add(builder.createHead());
        body.body.add(builder.createLeg());
        return body.body;
    }

}
