package com.edu.util;


/**
 * @author Tangzhihao
 * @date 2018/4/12
 */

public class WeChatUtil {
    private static final String WECHA_CORPID = OpenConfig.getConfig("WECHAT_CORPID");

    public static void main(String[] args) {
        System.out.println(WECHA_CORPID);
    }
}
