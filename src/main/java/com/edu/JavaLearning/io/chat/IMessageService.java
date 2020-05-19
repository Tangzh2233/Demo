package com.edu.JavaLearning.io.chat;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 1:47 PM
 * 消息服务接口
 **/
public interface IMessageService<T> {

    /**
     * 消息发送
     *
     * @param data
     */
    void sendMessage(T data);

    /**
     * 消息收取
     *
     * @return
     */
    T receiveMessage();
}
