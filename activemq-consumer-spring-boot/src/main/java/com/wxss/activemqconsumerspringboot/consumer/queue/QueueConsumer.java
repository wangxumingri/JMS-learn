//package com.wxss.activemqconsumerspringboot.consumer.queue;
//
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
///**
// * Author:Created by wx on 2019/7/17
// * Desc:
// */
//@Component
//public class QueueConsumer {
//    @JmsListener(destination = "queue-persist")
//    public void receiveMessage(Object message){
//        if (message != null){
//            System.out.println(message);
//        }
//    }
//}
