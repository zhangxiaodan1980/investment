package com.gov.investment.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class ActiveMQConfig {
    
    // 邮件队列名称
    private static final String EMAIL_QUEUE = "emailQueue";
    
    @Bean
    public Queue emailQueue() {
        return new ActiveMQQueue(EMAIL_QUEUE);
    }
}
