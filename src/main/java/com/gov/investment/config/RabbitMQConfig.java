package com.gov.investment.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // 主要业务队列
    @Value("${rabbitmq.queue.main}")
    private String mainQueue;

    // 延迟队列
    @Value("${rabbitmq.queue.delay}")
    private String delayQueue;

    // 死信队列
    @Value("${rabbitmq.queue.deadletter}")
    private String deadLetterQueue;

    // 主要交换机
    @Value("${rabbitmq.exchange.main}")
    private String mainExchange;

    // 延迟交换机
    @Value("${rabbitmq.exchange.delay}")
    private String delayExchange;

    // 死信交换机
    @Value("${rabbitmq.exchange.deadletter}")
    private String deadLetterExchange;

    // 主要路由键
    @Value("${rabbitmq.routingkey.main}")
    private String mainRoutingKey;

    // 延迟路由键
    @Value("${rabbitmq.routingkey.delay}")
    private String delayRoutingKey;

    // 死信路由键
    @Value("${rabbitmq.routingkey.deadletter}")
    private String deadLetterRoutingKey;

    /**
     * 主要业务队列
     */
    @Bean
    public Queue mainQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", delayExchange);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", delayRoutingKey);
        return QueueBuilder.durable(mainQueue).withArguments(args).build();
    }

    /**
     * 延迟队列
     */
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", mainExchange);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", mainRoutingKey);
        // 设置队列类型为延迟队列
        args.put("x-queue-type", "delay");
        return QueueBuilder.durable(delayQueue).withArguments(args).build();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(deadLetterQueue).build();
    }

    /**
     * 主要交换机
     */
    @Bean
    public DirectExchange mainExchange() {
        return ExchangeBuilder.directExchange(mainExchange).durable(true).build();
    }

    /**
     * 延迟交换机
     */
    @Bean
    public DirectExchange delayExchange() {
        return ExchangeBuilder.directExchange(delayExchange).durable(true).build();
    }

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(deadLetterExchange).durable(true).build();
    }

    /**
     * 绑定主要队列到主要交换机
     */
    @Bean
    public Binding mainBinding() {
        return BindingBuilder.bind(mainQueue()).to(mainExchange()).with(mainRoutingKey);
    }

    /**
     * 绑定延迟队列到延迟交换机
     */
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(delayRoutingKey);
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(deadLetterRoutingKey);
    }
}