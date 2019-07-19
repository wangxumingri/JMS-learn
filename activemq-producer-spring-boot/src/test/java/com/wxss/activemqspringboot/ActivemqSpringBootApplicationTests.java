package com.wxss.activemqspringboot;

import com.wxss.activemqspringboot.queue.QueueProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivemqSpringBootApplicationTests {

    @Autowired
    private QueueProducer queueProducer;
    @Test
    public void testSend() {
//        jmsTemplate.send("queue-persist", new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                TextMessage textMessage = session.createTextMessage("queue发送消息...");
//                textMessage.setStringProperty("level", "vip"); // 设置消息属性
//                return textMessage;
//            }
//        });
    }
    @Test
    public void testSendSimpleQueue(){
        queueProducer.sendSimpleMessage("queue-persist", "queue---持久性消息测试");
    }

    @Test
    public void testSendComplexMessage(){
        Properties properties = new Properties();
        properties.setProperty("level", "vip");
        properties.setProperty("persist","NON_PERSISTENT");
        queueProducer.sendComplexMessage("queue-complex","queue-----持久性消息测试",properties);
    }
}
