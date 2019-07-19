package com.edu.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tangzh on 2018/8/3.
 * dateStamp(getPreOrNextDate(),format)
 * 获取下一天零点的时间戳:1 获取下一天的时间，然后格式化为yyyyMMdd[目的是获取零点]
 *                    2 sdf.parse(strDate,"yyyyMMdd").getTime() 获取零点时间戳
 */
public class DateUtil {
    private final static String YYYYMMDD = "yyyyMMdd";
    private final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    private final static String YYYYMMDDHHSS = "yyyy-MM-dd:HH:mm:ss";


    public static String getCurDateForDay(){
        return forMatDate(new Date(),YYYYMMDD);
    }
    public static String getCurDateForHour(){
        return forMatDate(new Date(),YYYYMMDDHHMMSS);
    }

    //格式化日期
    public static String forMatDate(Date date,String fromat){
        return DateFormatUtils.format(date,fromat);
    }
    // string->long 获取指定日期的时间戳
    public static long dateStamp(String date,String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date parse = sdf.parse(date);
        System.out.println(parse);
        return parse.getTime();
    }

    /**
      * @descriptions:获取当前时间点的前后n的field的时间点
      * eg：field = Calendar.HOUR_OF_DAY,n = +/-2
      * return  date的后/前两个小时的时间点
    **/
    public static String getPreOrNextDate(Date date,int field,int n,String format){
        return forMatDate(getPreOrNextDateStamp(date,field,n),format);
    }
    public static Date getPreOrNextDateStamp(Date date,int field,int n){
        Calendar instace = getCalendarInstance(date);
        instace.add(field,n);
        return instace.getTime();
    }

    //获取Calendar实例
    public static Calendar getCalendarInstance(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    /**
     * @return 获取当前系统时间。单位:秒
     */
    public static long unixTime(){
        return System.currentTimeMillis()/1000L;
    }


    public static void main(String[] args) throws ParseException {
//        System.out.println(getCurDateForDay());
//        System.out.println(getCurDateForHour());
        System.out.println(new Date());
        String nextDate = getPreOrNextDate(new Date(), Calendar.HOUR_OF_DAY, 1, YYYYMMDD);
        System.out.println(dateStamp(nextDate,YYYYMMDD));
//        String nextDay = getPreOrNextDate(new Date(), Calendar.DAY_OF_MONTH, 0,YYYYMMDD);
//        System.out.println(nextDay+"::"+dateStamp(nextDay,YYYYMMDD));
//        System.out.println("nowStamp:"+new Date().getTime());
    }
//1542643200000

}
