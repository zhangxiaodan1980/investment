package com.gov.investment.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.normal}")
    private String normalQueue;

    @Value("${rabbitmq.queue.delay}")
    private String delayQueue;

    @Value("${rabbitmq.queue.dlq}")
    private String dlqQueue;

    @Value("${rabbitmq.exchange.normal}")
    private String normalExchange;

    @Value("${rabbitmq.exchange.delay}")
    private String delayExchange;

    @Value("${rabbitmq.exchange.dlq}")
    private String dlqExchange;

    @Value("${rabbitmq.routing.key.normal}")
    private String normalRoutingKey;

    @Value("${rabbitmq.routing.key.delay}")
    private String delayRoutingKey;

    @Value("${rabbitmq.routing.key.dlq}")
    private String dlqRoutingKey;

    @Value("${rabbitmq.ttl.delay}")
    private long delayTtl;

    @Value("${rabbitmq.ttl.dlq}")
    private long dlqTtl;

    // Dead Letter Queue
    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(dlqQueue)
                .withArgument("x-message-ttl", dlqTtl)
                .build();
    }

    // Delay Queue
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", dlqExchange);
        args.put("x-dead-letter-routing-key", dlqRoutingKey);
        args.put("x-message-ttl", delayTtl);
        return QueueBuilder.durable(delayQueue)
                .withArguments(args)
                .build();
    }

    // Normal Queue
    @Bean
    public Queue normalQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", delayExchange);
        args.put("x-dead-letter-routing-key", delayRoutingKey);
        return QueueBuilder.durable(normalQueue)
                .withArguments(args)
                .build();
    }

    // Dead Letter Exchange
    @Bean
    public DirectExchange dlqExchange() {
        return ExchangeBuilder.directExchange(dlqExchange)
                .durable(true)
                .build();
    }

    // Delay Exchange
    @Bean
    public DirectExchange delayExchange() {
        return ExchangeBuilder.directExchange(delayExchange)
                .durable(true)
                .build();
    }

    // Normal Exchange
    @Bean
    public DirectExchange normalExchange() {
        return ExchangeBuilder.directExchange(normalExchange)
                .durable(true)
                .build();
    }

    // Bind dlqQueue to dlqExchange
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue())
                .to(dlqExchange())
                .with(dlqRoutingKey);
    }

    // Bind delayQueue to delayExchange
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue())
                .to(delayExchange())
                .with(delayRoutingKey);
    }

    // Bind normalQueue to normalExchange
    @Bean
    public Binding normalBinding() {
        return BindingBuilder.bind(normalQueue())
                .to(normalExchange())
                .with(normalRoutingKey);
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Rabbit Template
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
