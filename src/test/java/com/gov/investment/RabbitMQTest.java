package com.gov.investment;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 主要交换机
    @Value("${rabbitmq.exchange.main}")
    private String mainExchange;

    // 主要路由键
    @Value("${rabbitmq.routingkey.main}")
    private String mainRoutingKey;

    @Test
    public void testSendMessage() {
        // 发送测试消息到主要队列
        String message = "Hello, RabbitMQ! This is a test message.";
        rabbitTemplate.convertAndSend(mainExchange, mainRoutingKey, message);
        System.out.println("Test message sent: " + message);

        // 等待一段时间，让消息处理完成
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}