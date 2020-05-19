package com.edu.JavaLearning.io.chat.common;

import java.nio.ByteBuffer;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 5:48 PM
 **/
public class QueryChatListRequest implements Message{

    private static final long serialVersionUID = 3172962586451883978L;
    private String chatId;
    private String memberId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public static void main(String[] args) {
        QueryChatListRequest request = new QueryChatListRequest();
        request.setMemberId("999655454931939329");
        request.setChatId("G1109530606053617715");
    }


    @Override
    public String toString() {
        return "QueryChatListRequest{" +
                "chatId='" + chatId + '\'' +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}
