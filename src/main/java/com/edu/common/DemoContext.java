package com.edu.common;

import com.edu.util.PPSUtil;

/**
 * @Author: tangzh
 * @Date: 2019/1/11$ 7:21 PM$
 **/
public class DemoContext {
    public static String TESTDEMO;

    public static void initDemoContext(PPSUtil ppsUtil){
        TESTDEMO = ppsUtil.getValue("WECHAT_CORPID").getValue();
    }
}
