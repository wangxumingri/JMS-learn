package com.wxss.activemqconsumerspringboot;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.*;
import java.io.IOException;

/**
 * Author:Created by wx on 2019/7/17
 * Desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NativeApiTests {
    private static Connection connection;
    @Value("${spring.activemq.broker-url}")
    private String brokeUrl;

    @Before
    public void beforeTest() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokeUrl);
         connection = connectionFactory.createConnection();

    }

    @Test
    public void testQueueReceive() throws JMSException {
        // 开启连接
        connection.start();
        // 创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建目的地
        Queue destination = session.createQueue("native-queue");
        // 创建消费者
        MessageConsumer consumer = session.createConsumer(destination);
        Message message = consumer.receive();
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("消息内容:"+textMessage.getText());
            String level = message.getStringProperty("level");
            System.out.println("消息的属性:"+level);
            System.out.println("原生API：queue消息接收成功...");

            message = consumer.receive(10000);
        }
        session.close();
        connection.close();
        System.out.println("消费者关闭...");
    }

    @Test
    public void testTopicReceive1() throws JMSException {
        // 开启连接
        connection.start();
        // 创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建目的地
        Destination destination = session.createTopic("native-topic");
        // 创建消费者
        MessageConsumer consumer = session.createConsumer(destination);
        Message message = consumer.receive();
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("消息内容:"+textMessage.getText());
            String level = message.getStringProperty("level");
            System.out.println("消息的属性:"+level);
            System.out.println("原生API：消息接收成功...");

            message = consumer.receive(10000);
        }
        session.close();
        connection.close();
        System.out.println("消费者关闭...");
    }

    @Test
    public void testTopicReceivePersist() throws JMSException {
        connection.setClientID("456789");
        // 创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建目的地
        Topic topic = session.createTopic("native-topic-persist");
        // 消费消息时，直接通过订阅消费，订阅方，在上线后，就能收到未上线前，发布方发布的的消息
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "测试");
//        session.createDurableSubscriber(topic, "测试");

        // 创建消费者：还是不能接收未上线前的消息
//        MessageConsumer consumer = session.createConsumer(topic);
//        Message message = consumer.receive();


        connection.start();

        Message message = topicSubscriber.receive();
        // 开启连接
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("消息内容:"+textMessage.getText());
            String level = message.getStringProperty("level");
            System.out.println("消息的属性:"+level);
            System.out.println("原生API：消息接收成功...");

            message = topicSubscriber.receive(3000);
        }
        session.close();
        connection.close();
        System.out.println("消费者关闭...");
    }

    @Test
    public void testAck() throws JMSException {
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        try {
            Queue des = session.createQueue("sb");
            MessageConsumer consumer = session.createConsumer(des);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
//                    System.out.println(message);
//                    ActiveMQTextMessage message1 = (ActiveMQTextMessage) message;
//                    int redeliveryCounter = message1.getRedeliveryCounter();
//                    System.out.println(redeliveryCounter);
                    System.out.println(message);
                    try {
                        message.acknowledge();
                    } catch (JMSException e) {
                    }
                }
            });

            System.in.read();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
            connection.close();
        }


    }
}


