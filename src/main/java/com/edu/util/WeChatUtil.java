package com.edu.util;


import com.edu.common.DemoContext;

/**
 * @author Tangzhihao
 * @date 2018/4/12
 */

public class WeChatUtil {
    private static String WECHA_CORPID;

    public static void main(String[] args) {
        DemoContext.initDemoContext(new PPSUtil("config"));
        WECHA_CORPID = DemoContext.TESTDEMO;
        System.out.println(WECHA_CORPID);
    }
}
