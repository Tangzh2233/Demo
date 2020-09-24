package com.edu.service.message;

import com.alibaba.fastjson.JSON;
import com.edu.config.DemoContextHolder;
import com.edu.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/9/19 21:06
 * @description Redis 订阅channel，做ch
 **/
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final static Logger log = LoggerFactory.getLogger(WebSocketHandler.class);

    @Value("${redis.subscribe.channel}")
    private String subscribeChannel;

    private static ConcurrentHashMap<String, WebSocketSession> SESSION_CONTEXT = new ConcurrentHashMap<>(256);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userNo = DemoContextHolder.getData().getUserNo();
        SESSION_CONTEXT.put(userNo, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userNo = DemoContextHolder.getData().getUserNo();
        SESSION_CONTEXT.remove(userNo);
    }


    public void pushMessage(String userId, String text) {
        Message message = new Message(userId, text);
        RedisUtil.publish(subscribeChannel, JSON.toJSONString(message));
    }

    @PostConstruct
    public void init() {
        Thread thread = new Thread(new MessageHandlerTask());
        thread.setName("MessageHandlerThread");
        System.out.println("MessageHandlerThread start ........");
        thread.start();
    }

    class MessageHandlerTask implements Runnable {

        @Override
        public void run() {
            try (Jedis jedis = RedisUtil.getPool().getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        try {
                            if (StringUtils.isNotBlank(message)) {
                                Message data = JSON.parseObject(message, Message.class);
                                if (!SESSION_CONTEXT.containsKey(data.getUserId())) {
                                    return;
                                }
                                WebSocketSession socketSession = SESSION_CONTEXT.get(data.getUserId());
                                if (socketSession.isOpen()) {
                                    socketSession.sendMessage(new TextMessage(data.getMessageBody()));
                                }
                            }
                        } catch (Exception e) {
                            log.error("消息处理异常", e);
                        }
                    }
                }, subscribeChannel);
            } catch (Exception e) {
                log.error("消息订阅异常", e);
            }
        }
    }

    static class Message {
        private String userId;
        private String messageBody;

        public Message() {
        }

        public Message(String userId, String messageBody) {
            this.userId = userId;
            this.messageBody = messageBody;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMessageBody() {
            return messageBody;
        }

        public void setMessageBody(String messageBody) {
            this.messageBody = messageBody;
        }

    }
}
