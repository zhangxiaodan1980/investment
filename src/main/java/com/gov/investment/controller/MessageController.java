package com.gov.investment.controller;

import com.gov.investment.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.MAIN_EXCHANGE,
                RabbitMQConfig.MAIN_ROUTING_KEY,
                message);
        return "Message sent successfully: " + message;
    }
}
