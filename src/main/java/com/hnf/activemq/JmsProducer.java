package com.hnf.activemq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JmsProducer {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("producer.xml");
        // 获取提供者接口实例
        ProducerService producerService = context.getBean(ProducerService.class);
        for (int i = 0; i < 100; i++) {
            // 调用发送消息方法
            producerService.sendMessage("消息发送来了" + i);
        }
        //关闭连接
        context.close();
    }

}