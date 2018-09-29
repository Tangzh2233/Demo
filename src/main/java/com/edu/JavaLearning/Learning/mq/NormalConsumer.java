package com.edu.JavaLearning.Learning.mq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tangzhihao
 * @date 2017/10/17
 * 消费器
 */

public class NormalConsumer{

    private final static Logger logger = LoggerFactory.getLogger(NormalConsumer.class);
    private DefaultMQPushConsumer consumer;
    private String strAddr;
    private String groupName;

    public void init() throws MQClientException {

        try {
            //需要一个consumer group名字作为构造方法的参数，这里为consumer1
            consumer = new DefaultMQPushConsumer(groupName);
            //设置消费地址
            consumer.setNamesrvAddr(strAddr);
            //这里设置的是一个consumer的消费策略
            //CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
            //CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
            //CONSUME_FROM_TIMESTAMP 从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //设置consumer所订阅的Topic和Tag，*代表全部的Tag
            consumer.subscribe("topicTest","order_1");
            //设置一个Listener，主要进行消息的逻辑处理
            consumer.registerMessageListener(new MessageListenerImpl());
            consumer.start();
        }catch (Exception e){
            logger.info("RockerMq Consumer init Fail",e);
        }
    }
}
