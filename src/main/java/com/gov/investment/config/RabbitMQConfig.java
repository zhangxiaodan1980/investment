package com.gov.investment.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // 业务队列相关配置
    public static final String BUSINESS_QUEUE_NAME = "business.queue";
    public static final String BUSINESS_EXCHANGE_NAME = "business.exchange";
    public static final String BUSINESS_ROUTING_KEY = "business.routing.key";

    // 延迟队列相关配置
    public static final String DELAY_QUEUE_NAME = "delay.queue";
    public static final String DELAY_EXCHANGE_NAME = "delay.exchange";
    public static final String DELAY_ROUTING_KEY = "delay.routing.key";

    // 死信队列相关配置
    public static final String DEAD_LETTER_QUEUE_NAME = "dead.letter.queue";
    public static final String DEAD_LETTER_EXCHANGE_NAME = "dead.letter.exchange";
    public static final String DEAD_LETTER_ROUTING_KEY = "dead.letter.routing.key";

    // 最大重试次数
    public static final int MAX_RETRY_COUNT = 6;

    // 业务队列
    @Bean
    public Queue businessQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        return QueueBuilder.durable(BUSINESS_QUEUE_NAME)
                .withArguments(args)
                .build();
    }

    // 业务交换机
    @Bean
    public DirectExchange businessExchange() {
        return ExchangeBuilder.directExchange(BUSINESS_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    // 业务队列与业务交换机的绑定
    @Bean
    public Binding businessBinding() {
        return BindingBuilder.bind(businessQueue())
                .to(businessExchange())
                .with(BUSINESS_ROUTING_KEY);
    }

    // 延迟队列
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机（延迟队列的死信交换机是业务交换机）
        args.put("x-dead-letter-exchange", BUSINESS_EXCHANGE_NAME);
        // 设置死信路由键（延迟队列的死信路由键是业务路由键）
        args.put("x-dead-letter-routing-key", BUSINESS_ROUTING_KEY);
        // 设置队列类型为延迟队列
        args.put("x-queue-type", "delay");
        return QueueBuilder.durable(DELAY_QUEUE_NAME)
                .withArguments(args)
                .build();
    }

    // 延迟交换机
    @Bean
    public DirectExchange delayExchange() {
        return ExchangeBuilder.directExchange(DELAY_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    // 延迟队列与延迟交换机的绑定
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue())
                .to(delayExchange())
                .with(DELAY_ROUTING_KEY);
    }

    // 死信队列
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME)
                .build();
    }

    // 死信交换机
    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    // 死信队列与死信交换机的绑定
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }
}