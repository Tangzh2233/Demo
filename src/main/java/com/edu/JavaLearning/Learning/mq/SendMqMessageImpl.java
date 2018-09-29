package com.edu.JavaLearning.Learning.mq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tangzhihao
 * @date 2017/10/17
 */

public class SendMqMessageImpl {

    private DefaultMQProducer producer;
    private String namesrvaddr;
    private String groupId;


    public void init(){
        if(this.producer==null){
            producer= new DefaultMQProducer(this.groupId);
            producer.setNamesrvAddr(this.namesrvaddr);
        }
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:mm:dd");
        String strDate = sdf.format(date);
        for(int i=0;i<3;i++){
            String body = strDate + "order_"+i;
            Message message = new Message("topicTest","order_"+i,"key",body.getBytes());
            SendResult sendResult = this.sendMessage(message);
            System.out.println(sendResult.getSendStatus());
        }
    }

    public SendResult sendMessage(Message message){
        try {
            SendResult sendResult = this.producer.send(message);
            return sendResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    public void setProducer(DefaultMQProducer producer) {
        this.producer = producer;
    }

    public String getNamesrvaddr() {
        return namesrvaddr;
    }

    public void setNamesrvaddr(String namesrvaddr) {
        this.namesrvaddr = namesrvaddr;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
