package com.edu.common;

/**
 * @author Tangzhihao
 * @date 2018/3/26
 */

public enum EWarnChannel {
    E_MAIL("Email","邮件"),
    E_WEIXIN("WeiXin","企业微信"),
    E_SMS("SMS","短信");


    private String code;
    private String desc;
    EWarnChannel(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
