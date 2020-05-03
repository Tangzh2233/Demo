package com.edu.JavaLearning.设计模式.状态模式;

import com.alibaba.fastjson.JSON;
import com.edu.dao.domain.User;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/8 7:19 PM
 **/
public class Context {

    public State O = new StateO(this);
    public State A = new StateA(this);
    public State B = new StateB(this);
    public State C = new StateC(this);
    public State D = new StateD(this);
    public State state = O;
    public State lastState = O;



    public void doing(){
        lastState = state;
        state.doNext(state.getService());
    }

    public static void main(String[] args) throws ParseException {
        int compareTo = "1068578502996885509".compareTo("537243655923409109");
        System.out.println(compareTo);
//        Context context = new Context();
//        while (context.state.getService() != 0){
//            context.doing();
//        }
    }

    public void setState(State state) {
        this.state = state;
    }
}
