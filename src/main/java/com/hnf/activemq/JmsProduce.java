package com.hnf.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {

    //声明服务器地址
    private static final String url = "tcp://127.0.0.1:61616";
    //声明队列名称
    //private static final String queue = "queue_test";
    private static final String topic = "topic_test";
    public static void main(String []args)throws Exception{
  
        //创建连接工厂  由消息服务商提供
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        //根据消息工厂创建连接
        Connection connection = factory.createConnection();
        //开启连接
        connection.start();
        //根据连接创建会话   参数一  是否使用事务  参数二 应答模式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建目标  也就是队列
        // Destination destination =  session.createQueue(JmsProduce.queue);
        //创建主题目标
          Destination destination = session.createTopic(topic);
        //创建一个生产者
        MessageProducer producer = session.createProducer(destination);
        //
        for (int i=0;i<100;i++){
            //创建消息
            TextMessage textMessage = session.createTextMessage("test" + i);
            //生产者将消息发送给队列
            producer.send(textMessage);
            System.out.println("生产者"+textMessage);
        }
        connection.close();

    }
}