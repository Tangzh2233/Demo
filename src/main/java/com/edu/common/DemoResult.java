package com.edu.common;

import com.alibaba.fastjson.JSON;

/**
 * @author Tangzhihao
 * @date 2018/5/17
 */
public class DemoResult<T> {
    //响应状态 00 成功  01 失败
    private String status;
    //响应消息
    private String msg;
    //响应数据
    private T data;

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

    public void setData(T data) {
        this.data = data;
    }

    public DemoResult(){}

    private static <T> DemoResult<T> resultBuild(String status, String msg, T data){
        DemoResult<T> resp = new DemoResult<>();
        resp.setMsg(status);
        resp.setMsg(msg);
        resp.setData(data);
        return resp;
    }
    public static <T> DemoResult<T> ok(){
        return DemoResult.resultBuild("00","ok",null);
    }
    public static <T> DemoResult<T> ok(T data){
        return DemoResult.resultBuild("00","ok",data);
    }

    public static DemoResult fail(){
        return DemoResult.resultBuild("11","fail",null);
    }

    public static <T> DemoResult<T> fail(T data){
        return DemoResult.resultBuild("11","faik",data);
    }

    public boolean isOk(){
        return "00".equals(this.status);
    }

    public T getData(T a){

        return null;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        DemoResult.fail();
    }

}
class Test<T extends Comparable>{

    private T u;

    public <U extends Comparable> T getData(U a){
        a.compareTo("00");
        return null;
    }

}
