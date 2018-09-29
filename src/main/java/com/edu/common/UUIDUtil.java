package com.edu.common;

import java.util.UUID;

/**
 * @author tangzh
 * @create 2018/8/3
 */
public class UUIDUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
