package com.edu.JavaLearning.Learning.mq;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.edu.dao.domain.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Tangzhihao
 * @date 2017/10/17
 */

public class MessageListenerImpl implements MessageListenerConcurrently{

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt ext:list) {
                String data = new String(ext.getBody(), StandardCharsets.UTF_8);
                User user = JSONObject.parseObject(data, User.class);
                doConsume(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    public void doConsume(Object ext){
        System.out.println("具体业务逻辑处理");
    }
}
