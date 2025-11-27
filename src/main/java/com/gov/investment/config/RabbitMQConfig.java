package com.gov.investment.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.api.MessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.core.AcknowledgeMode;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // Queue names
    public static final String MAIN_QUEUE = "main.queue";
    public static final String DELAY_QUEUE = "delay.queue";
    public static final String DEAD_LETTER_QUEUE = "dead.letter.queue";

    // Exchange names
    public static final String MAIN_EXCHANGE = "main.exchange";
    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";

    // Routing keys
    public static final String MAIN_ROUTING_KEY = "main.routing.key";
    public static final String DELAY_ROUTING_KEY = "delay.routing.key";
    public static final String DEAD_LETTER_ROUTING_KEY = "dead.letter.routing.key";

    // Delay configuration
    public static final long DELAY_TIME = 10000; // 10 seconds
    public static final int MAX_RETRY_ATTEMPTS = 6;

    // Main queue
    @Bean
    public Queue mainQueue() {
        Map<String, Object> args = new HashMap<>();
        // Set dead-letter exchange and routing key
        args.put("x-dead-letter-exchange", DELAY_EXCHANGE);
        args.put("x-dead-letter-routing-key", DELAY_ROUTING_KEY);
        return QueueBuilder.durable(MAIN_QUEUE)
                .withArguments(args)
                .build();
    }

    // Delay queue
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        // Set dead-letter exchange and routing key
        args.put("x-dead-letter-exchange", MAIN_EXCHANGE);
        args.put("x-dead-letter-routing-key", MAIN_ROUTING_KEY);
        // Set delay time
        args.put("x-message-ttl", DELAY_TIME);
        return QueueBuilder.durable(DELAY_QUEUE)
                .withArguments(args)
                .build();
    }

    // Dead-letter queue
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE)
                .build();
    }

    // Main exchange
    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    // Delay exchange
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    // Dead-letter exchange
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // Main queue binding
    @Bean
    public Binding mainQueueBinding(Queue mainQueue, DirectExchange mainExchange) {
        return BindingBuilder.bind(mainQueue)
                .to(mainExchange)
                .with(MAIN_ROUTING_KEY);
    }

    // Delay queue binding
    @Bean
    public Binding delayQueueBinding(Queue delayQueue, DirectExchange delayExchange) {
        return BindingBuilder.bind(delayQueue)
                .to(delayExchange)
                .with(DELAY_ROUTING_KEY);
    }

    // Dead-letter queue binding
    @Bean
    public Binding deadLetterQueueBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(DEAD_LETTER_ROUTING_KEY);
    }

    // Rabbit template with JSON message converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    // Rabbit listener container factory
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
