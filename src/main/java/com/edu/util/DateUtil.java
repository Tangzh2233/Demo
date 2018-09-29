package com.edu.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tangzh on 2018/8/3.
 */
public class DateUtil {
    private final static String YYYYMMDD = "yyyyMMdd";
    private final static String YYYYMMDDHHSS = "yyyy-MM-dd:HH:mm:ss";


    public static String getCurDateForDay(){
        return forMatDate(new Date(),YYYYMMDD);
    }
    public static String getCurDateForHour(){
        return forMatDate(new Date(),YYYYMMDDHHSS);
    }

    //格式化日期
    public static String forMatDate(Date date,String fromat){
        return DateFormatUtils.format(date,fromat);
    }

    /**
      * @descriptions:获取当前时间点的前后n的field的时间点
      * eg：field = Calendar.HOUR_OF_DAY,n = +/-2
      * return  date的后/前两个小时的时间点
    **/
    public static String getPreOrNextDate(Date date,int field,int n){
        Calendar instace = getCalendar(date);
        instace.add(field,n);
        return forMatDate(instace.getTime(),YYYYMMDDHHSS);
    }

    public static Calendar getCalendar(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }


    public static void main(String[] args) {
        System.out.println(getCurDateForDay());
        System.out.println(getCurDateForHour());
        System.out.println(getPreOrNextDate(new Date(),Calendar.MINUTE,-10));
    }


}
