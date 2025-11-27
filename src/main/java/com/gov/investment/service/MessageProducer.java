package com.gov.investment.service;

import com.gov.investment.config.RabbitMQConfig;
import com.gov.investment.model.MessageContent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到业务队列
     * @param content 消息内容
     * @return 消息ID
     */
    public String sendMessageToBusinessQueue(String content) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMessageId(UUID.randomUUID().toString());
        messageContent.setContent(content);
        messageContent.setCreateTime(System.currentTimeMillis());
        messageContent.setLastProcessTime(System.currentTimeMillis());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BUSINESS_EXCHANGE_NAME,
                RabbitMQConfig.BUSINESS_ROUTING_KEY,
                messageContent);

        return messageContent.getMessageId();
    }

    /**
     * 发送消息到延迟队列
     * @param content 消息内容
     * @param delayTime 延迟时间（毫秒）
     * @param retryCount 重试次数
     * @return 消息ID
     */
    public String sendMessageToDelayQueue(String content, long delayTime, int retryCount) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMessageId(UUID.randomUUID().toString());
        messageContent.setContent(content);
        messageContent.setRetryCount(retryCount);
        messageContent.setCreateTime(System.currentTimeMillis());
        messageContent.setLastProcessTime(System.currentTimeMillis());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DELAY_EXCHANGE_NAME,
                RabbitMQConfig.DELAY_ROUTING_KEY,
                messageContent,
                message -> {
                    message.getMessageProperties().setDelay((int) delayTime);
                    return message;
                });

        return messageContent.getMessageId();
    }

    /**
     * 发送消息到死信队列
     * @param content 消息内容
     * @param retryCount 重试次数
     * @return 消息ID
     */
    public String sendMessageToDeadLetterQueue(String content, int retryCount) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMessageId(UUID.randomUUID().toString());
        messageContent.setContent(content);
        messageContent.setRetryCount(retryCount);
        messageContent.setCreateTime(System.currentTimeMillis());
        messageContent.setLastProcessTime(System.currentTimeMillis());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DEAD_LETTER_EXCHANGE_NAME,
                RabbitMQConfig.DEAD_LETTER_ROUTING_KEY,
                messageContent);

        return messageContent.getMessageId();
    }
}