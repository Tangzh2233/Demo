package com.edu.JavaLearning.webSocket;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/11 2:16 PM
 * webSocket Server
 **/
@ServerEndpoint("/webSocket/chatList/{memberId}")
public class WebSocketServer {


    private static Map<String, Client> holdClients = new ConcurrentHashMap<>();

    @OnOpen
    public void open(@PathParam("memberId") String memberId, Session session) {
        Client client = new Client(memberId, session);
        holdClients.put(memberId, client);
        System.out.println(memberId + "已连接.....");
    }

    @OnMessage
    public void onMessage(String data){
        Message message = JSON.parseObject(data, Message.class);
        sendMessageTo(message);
    }

    public void sendMessageTo(Message data){
        Client client = holdClients.get(data.getReceiver());
        client.getSession().getAsyncRemote().sendText(data.getContent());
    }
}


@Data
class Client {
    private String memberId;
    private Session session;

    public Client(String memberId, Session session) {
        this.memberId = memberId;
        this.session = session;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}

@Data
class Message {
    private String sender;
    private String receiver;
    private String content;

    public Message() {
    }

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
