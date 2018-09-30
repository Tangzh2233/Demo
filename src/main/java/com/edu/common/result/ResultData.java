package com.edu.common.result;

import com.edu.common.Constants;

import java.io.Serializable;

public class ResultData<T> implements Serializable {

    private static final long serialVersionUID = 907734037937871405L;

    private String rspCode;
    private String rspMessage;
    private T data;

    public ResultData() {
    }

    public ResultData(String rspCode, String rspMessage) {
        this.rspCode = rspCode;
        this.rspMessage = rspMessage;
    }

    public ResultData(String rspCode, String rspMessage, T data) {
        this.rspCode = rspCode;
        this.rspMessage = rspMessage;
        this.data = data;
    }

    public static <T> ResultData<T> isSuccess(String msg,T data){
        return new ResultData<T>("00",msg,data);
    }

    public static ResultData defaultSuccess(){
        return new ResultData(Constants.SUCCESS_CODE,"处理成功");
    }
    public static ResultData isFail(String code,String msg){
        return new ResultData(code,msg);
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMessage() {
        return rspMessage;
    }

    public void setRspMessage(String rspMessage) {
        this.rspMessage = rspMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "rspCode='" + rspCode + '\'' +
                ", rspMessage='" + rspMessage + '\'' +
                ", data=" + data +
                '}';
    }
}
