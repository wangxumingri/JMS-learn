package com.wxss.activemqspringboot.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.jms.*;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * Author:Created by wx on 2019/7/17
 * Desc:
 */
@Component
public class QueueProducer {
    @Autowired
    private JmsTemplate jmsTemplate;


    public void sendSimpleMessage(String destination,Object message){
        Assert.notNull(destination, "目的地不能为null");
//        Assert.isNull(message, "消息体不能为null");
        jmsTemplate.convertAndSend(destination, message);
    }

    public void sendComplexMessage(String destination, Object messageBody,Properties properties){
        Assert.notNull(destination, "目的地不能为null");
        Assert.notNull(messageBody, "消息体不能为null");

        if (properties == null){
            jmsTemplate.convertAndSend(destination, messageBody);
        }else {
            jmsTemplate.send(destination, session -> {
                Message message;
                if (messageBody instanceof String){
                    message = session.createTextMessage(messageBody.toString());
                }else {
                    throw new IllegalArgumentException("目前支持的消息体类型只有String,但是传递的消息体类型为"+messageBody.getClass());
                }

                String persistProperty = properties.getProperty("persist","PERSISTENT");
                // 默认持久化
                if ("NON_PERSISTENT".equalsIgnoreCase(persistProperty)){
                    message.setJMSDeliveryMode(1);
                }else {
                    message.setJMSDeliveryMode(2);
                }
                // 设置消息属性
                String levelProperty = properties.getProperty("level", "Ordinary users");
                message.setStringProperty("level",levelProperty);
                return message;
            });
        }
    }
}
