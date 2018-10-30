package com.edu.common;

import com.alibaba.fastjson.JSON;

/**
 * @author Tangzhihao
 * @date 2018/5/17
 */
public class DemoResult {
    //响应状态 00 成功  01 失败
    private String status;
    //响应消息
    private String msg;
    //响应数据
    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public DemoResult(){}
    public DemoResult(String status, String msg, Object data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static DemoResult resultBuild(String status,String msg,Object data){
        return new DemoResult(status,msg,data);
    }
    public static DemoResult ok(){
        return DemoResult.resultBuild("00","ok",null);
    }
    public static DemoResult ok(Object data){
        return DemoResult.resultBuild("00","ok",data);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
