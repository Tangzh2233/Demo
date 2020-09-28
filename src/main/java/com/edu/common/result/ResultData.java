package com.edu.common.result;


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

    public static ResultData defaultSuccess(Object data){
        return new ResultData<>(ERspCode.SUCCESS.getCode(),ERspCode.SUCCESS.getMsg(),data);
    }

    public static ResultData defaultSuccess(){
        return defaultSuccess(null);
    }

    public static ResultData defaultFail(Object data){
        return new ResultData<>(ERspCode.FAIL.getCode(),String.valueOf(data));
    }

    public static boolean checkSuccess(ResultData result){
        return ERspCode.SUCCESS.getCode().equals(result.getRspCode());
    }

    public boolean isSuccess(){
        return ERspCode.SUCCESS.getCode().equals(this.rspCode);
    }

    public static ResultData defaultFail(){
        return defaultFail(null);
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
