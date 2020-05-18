package com.edu.JavaLearning.Netty.longpolling;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/11 7:40 PM
 **/
public class TcpRequest {

    private String reqKey;

    private String chatId;

    private String content;

    public String getReqKey() {
        return reqKey;
    }

    public void setReqKey(String reqKey) {
        this.reqKey = reqKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
