package com.edu.JavaLearning.juc.atomic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: tangzh
 * @Date: 2019/4/16$ 8:15 PM$
 * UnSafe:java代码直接访问内存 https://www.cnblogs.com/throwable/p/9139947.html
 * volatile:修饰变量不可被线程缓存,每次数据均从主存读取。
 *
 *
 **/
public class Stu_Atomic{

    private AtomicInteger atomicInteger = new AtomicInteger();

    public static final long ONE_HOUR = 60 * 60 * 1000L;

    public static final long ONE_DAY = 24 * ONE_HOUR;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");

    public static void main(String[] args) {
        Date date = new Date(1544281000000L);
        String format = sdf.format(date);
        System.out.println(format);
    }

}
