package com.edu.common.result;


/**
 * @Author: tangzh
 * @Date: 2019/6/28$ 11:43 AM$
 **/
public enum ERspCode {

    SUCCESS("200","处理成功"),
    FAIL("500","处理失败"),
    ;

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ERspCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
