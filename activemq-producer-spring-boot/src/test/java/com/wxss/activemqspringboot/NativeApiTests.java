package com.wxss.activemqspringboot;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.jms.*;

/**
 * Author:Created by wx on 2019/7/17
 * Desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NativeApiTests {

    private static Connection connection;

    @Value("${spring.activemq.broker-url}") // 不使用占位符时，直接赋值
    private String url;

    @Before
    public void beforeTest() throws JMSException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        // 创建连接
        connection = connectionFactory.createConnection();
    }

    @Test
    public void testAck() throws JMSException {

        Session session = null;
        try {
            // 开启连接
            connection.start();
            // 创建会话
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // 创建目的地
            Queue Queue = session.createQueue("sb");
            // 创建生产者
            MessageProducer producer = session.createProducer(Queue);
            // 创建消息
            Message message = session.createTextMessage("dsb");
            // 设置消息持久化
//            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // 发送消息
            producer.send(Queue, message);
            System.out.println("原生API：topic消息发送成功...");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            Assert.notNull(session, "session关闭异常：null");
            session.close();
            connection.close();
        }

    }

    public void sendMessage(String destination, String type, Object messageBody, int deliveryMode) throws JMSException {
        Session session = null;
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination ds = null;
            if ("queue".equalsIgnoreCase(type)) {
                ds = session.createQueue(destination);
            } else if ("topic".equalsIgnoreCase(type)) {
                ds = session.createTopic(destination);
            } else {
                throw new IllegalArgumentException("消息类型参数错误");
            }
            MessageProducer producer = session.createProducer(ds);
            producer.setDeliveryMode(deliveryMode);

            connection.start();

            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(messageBody.toString());
            producer.send(ds, textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            Assert.notNull(session, "session关闭异常：null");
            session.close();
            connection.close();
        }

    }
    @Test
    public void sendTopic() throws JMSException {
        Session session = null;
        try {

            // 创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建目的地
            Topic topic = session.createTopic("native-topic-persist");
            // 创建生产者
            MessageProducer producer = session.createProducer(topic);
            // 创建消息
            Message message = session.createTextMessage("原生API：native-persist消息持久性测试");
            // 设置消息属性
            message.setStringProperty("level", "VIP");
            // 设置消息持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);


            // 开启连接
            connection.start();
            // 发送消息
            producer.send(topic, message);
            System.out.println("原生API：topic消息发送成功...");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            Assert.notNull(session, "session关闭异常：null");
            session.close();
            connection.close();
        }
}

    @Test
    public void send() throws JMSException {
        Session session = null;
        try {
            // 开启连接
            connection.start();
            // 创建会话
             session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建目的地
            Queue destination = session.createQueue("native-queue");
            // 创建生产者
            MessageProducer producer = session.createProducer(destination);
            // 创建消息
            Message message = session.createTextMessage("原生API：queue消息持久性测试");
            // 设置消息属性
            message.setStringProperty("level", "VIP");
            // 设置消息持久化
//            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // 设置消息并非持久化
//            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 发送消息
            producer.send(destination, message);
            System.out.println("原生API：queue消息发送成功...");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            Assert.notNull(session, "session关闭异常：null");
            session.close();
            connection.close();
        }
    }

    @Test
    public void sendTopicMessage(){
        try {
            sendMessage("native-topic", "topic", "原生Api:topic消息持久性测试",2);
            System.out.println("native-topic消息发送成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
