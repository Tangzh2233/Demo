package com.edu.JavaLearning.jdktest.Calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Tangzhihao
 * @date 2018/5/8
 */

public class MyCalendar {
    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        //这只初始日期 2018-05-09:09:55
        beginTime.set(2018,4,9,9,55);
        //设置day_of_month = 28
        beginTime.set(Calendar.DAY_OF_MONTH,28);
    //    System.out.println(format.format(beginTime.getTime()));
        //设置月份=5。即表示6月份
        beginTime.set(Calendar.MONTH,5);
        //beginTime月份+12
        beginTime.add(Calendar.MONTH,12);
    //    beginTime.add(Calendar.HOUR,-24);
        endTime.add(Calendar.MINUTE,-10);
        System.out.println(format.format(beginTime.getTime()));
        //和add的区别:上一层级不变09-28 + 5 = 09-03 仅级别改变
        beginTime.roll(Calendar.DAY_OF_MONTH,5);
        //将Field设置为未定义
        beginTime.clear(Calendar.YEAR);
        System.out.println(format.format(beginTime.getTime()));
        System.out.println(format.format(endTime.getTime()));
        System.out.println("CompareTo: "+endTime.compareTo(beginTime));
    }
}
