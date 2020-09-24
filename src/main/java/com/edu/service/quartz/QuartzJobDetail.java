package com.edu.service.quartz;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/8/31 12:05
 * @description
 **/
public class QuartzJobDetail {

    public void execute(){
        System.out.println(QuartzJobDetail.class.getSimpleName());
    }
}
