package com.hnf.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumer {

    private static final String url="tcp://127.0.0.1:61616";
    //private static final String queue = "queue_test";
    private static final String topic = "topic_test";
    public static void main(String [] args) throws JMSException {

        //创建连接工厂  由消息服务商提供
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        //根据消息工厂创建连接
        Connection connection = factory.createConnection();
        //开启连接
        connection.start();
        //根据连接创建会话   参数一  是否使用事务  参数二 应答模式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建目标  也就是队列
        //Destination destination =  session.createQueue(queue);
         //创建主题目标
           Destination destination = session.createTopic(topic);
        //创建消费者
        MessageConsumer consumer = session.createConsumer(destination);
        //创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage message1 = (TextMessage) message;
                System.out.println("接收消息"+message1);
            }
        });
    }
}