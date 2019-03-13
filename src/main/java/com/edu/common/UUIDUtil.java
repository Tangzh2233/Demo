package com.edu.common;

import com.edu.util.ZipFileUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tangzh
 * @create 2018/8/3
 */
public class UUIDUtil {
    private static AtomicInteger ai = new AtomicInteger(0);

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }




}
