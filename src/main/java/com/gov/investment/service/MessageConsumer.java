package com.gov.investment.service;

import com.gov.investment.config.RabbitMQConfig;
import com.gov.investment.model.MessageContent;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private ExternalSystemNotificationService externalSystemNotificationService;

    @Autowired
    private HuaweiOBSService huaweiOBSService;

    /**
     * 业务队列消费者
     * @param messageContent 消息内容
     * @param message RabbitMQ消息对象
     * @param channel 通道对象
     * @throws IOException IO异常
     */
    @RabbitListener(queues = RabbitMQConfig.BUSINESS_QUEUE_NAME)
    public void handleBusinessMessage(@Payload MessageContent messageContent, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 处理消息
            boolean success = processMessage(messageContent);

            if (success) {
                // 消息处理成功，确认消息
                channel.basicAck(deliveryTag, false);
                logger.info("消息处理成功，消息ID: {}", messageContent.getMessageId());

                // 通知外部系统
                boolean notifySuccess = externalSystemNotificationService.notifyExternalSystem(
                        messageContent.getMessageId(), messageContent.getContent());
                if (!notifySuccess) {
                    logger.error("通知外部系统失败，消息ID: {}", messageContent.getMessageId());
                    // 这里可以根据业务需求决定是否需要重试通知
                }
            } else {
                // 消息处理失败，处理重试逻辑
                handleMessageFailure(messageContent, deliveryTag, channel);
            }
        } catch (Exception e) {
            logger.error("处理业务消息时发生异常，消息ID: {}", messageContent.getMessageId(), e);
            // 发生异常时，处理重试逻辑
            handleMessageFailure(messageContent, deliveryTag, channel);
        }
    }

    /**
     * 延迟队列消费者
     * @param messageContent 消息内容
     * @param message RabbitMQ消息对象
     * @param channel 通道对象
     * @throws IOException IO异常
     */
    @RabbitListener(queues = RabbitMQConfig.DELAY_QUEUE_NAME)
    public void handleDelayMessage(@Payload MessageContent messageContent, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 处理消息
            boolean success = processMessage(messageContent);

            if (success) {
                // 消息处理成功，确认消息
                channel.basicAck(deliveryTag, false);
                logger.info("延迟消息处理成功，消息ID: {}", messageContent.getMessageId());

                // 通知外部系统
                boolean notifySuccess = externalSystemNotificationService.notifyExternalSystem(
                        messageContent.getMessageId(), messageContent.getContent());
                if (!notifySuccess) {
                    logger.error("通知外部系统失败，消息ID: {}", messageContent.getMessageId());
                    // 这里可以根据业务需求决定是否需要重试通知
                }
            } else {
                // 消息处理失败，处理重试逻辑
                handleMessageFailure(messageContent, deliveryTag, channel);
            }
        } catch (Exception e) {
            logger.error("处理延迟消息时发生异常，消息ID: {}", messageContent.getMessageId(), e);
            // 发生异常时，处理重试逻辑
            handleMessageFailure(messageContent, deliveryTag, channel);
        }
    }

    /**
     * 死信队列消费者
     * @param messageContent 消息内容
     * @param message RabbitMQ消息对象
     * @param channel 通道对象
     * @throws IOException IO异常
     */
    @RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE_NAME)
    public void handleDeadLetterMessage(@Payload MessageContent messageContent, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            logger.info("收到死信队列消息，消息ID: {}, 重试次数: {}", 
                    messageContent.getMessageId(), messageContent.getRetryCount());

            // 上传消息到华为云OBS
            boolean uploadSuccess = huaweiOBSService.uploadToOBS(
                    messageContent.getMessageId(), messageContent.getContent());

            if (uploadSuccess) {
                // 上传成功，确认消息
                channel.basicAck(deliveryTag, false);
                logger.info("死信消息已成功上传到华为云OBS，消息ID: {}", messageContent.getMessageId());
            } else {
                // 上传失败，拒绝消息并重新入队（可以根据业务需求决定是否重试）
                channel.basicReject(deliveryTag, false);
                logger.error("死信消息上传到华为云OBS失败，已拒绝，消息ID: {}", messageContent.getMessageId());
            }
        } catch (Exception e) {
            logger.error("处理死信消息时发生异常，消息ID: {}", messageContent.getMessageId(), e);
            // 发生异常时，拒绝消息并重新入队（可以根据业务需求决定是否重试）
            channel.basicReject(deliveryTag, false);
        }
    }

    /**
     * 处理消息失败的逻辑
     * @param messageContent 消息内容
     * @param deliveryTag 消息标签
     * @param channel 通道对象
     * @throws IOException IO异常
     */
    private void handleMessageFailure(MessageContent messageContent, long deliveryTag, Channel channel) throws IOException {
        // 增加重试次数
        messageContent.setRetryCount(messageContent.getRetryCount() + 1);
        messageContent.setLastProcessTime(System.currentTimeMillis());

        if (messageContent.getRetryCount() < RabbitMQConfig.MAX_RETRY_COUNT) {
            // 重试次数未达到最大值，发送到延迟队列
            long delayTime = calculateDelayTime(messageContent.getRetryCount());
            messageProducer.sendMessageToDelayQueue(
                    messageContent.getContent(), 
                    delayTime, 
                    messageContent.getRetryCount());
            
            // 确认消息（从当前队列中移除）
            channel.basicAck(deliveryTag, false);
            
            logger.info("消息处理失败，已发送到延迟队列，消息ID: {}, 重试次数: {}, 延迟时间: {}ms", 
                    messageContent.getMessageId(), messageContent.getRetryCount(), delayTime);
        } else {
            // 重试次数达到最大值，拒绝消息并进入死信队列
            channel.basicReject(deliveryTag, false);
            logger.error("消息处理失败，重试次数已达最大值，已拒绝并进入死信队列，消息ID: {}, 重试次数: {}", 
                    messageContent.getMessageId(), messageContent.getRetryCount());
        }
    }

    /**
     * 计算延迟时间（指数退避算法）
     * @param retryCount 重试次数
     * @return 延迟时间（毫秒）
     */
    private long calculateDelayTime(int retryCount) {
        // 指数退避算法：baseDelay * (2 ^ (retryCount - 1))
        long baseDelay = 1000; // 基础延迟时间1秒
        return baseDelay * (long) Math.pow(2, retryCount - 1);
    }

    /**
     * 处理消息的具体逻辑
     * @param messageContent 消息内容
     * @return 处理是否成功
     */
    private boolean processMessage(MessageContent messageContent) {
        try {
            // TODO: 实现具体的消息处理逻辑
            // 这里可以是数据库操作、业务逻辑处理等
            logger.info("开始处理消息，消息ID: {}, 重试次数: {}", 
                    messageContent.getMessageId(), messageContent.getRetryCount());

            // 模拟消息处理失败的情况（可以根据业务需求调整）
            // if (messageContent.getRetryCount() < 3) {
            //     throw new RuntimeException("模拟消息处理失败");
            // }

            // 模拟消息处理成功
            return true;
        } catch (Exception e) {
            logger.error("处理消息时发生异常，消息ID: {}", messageContent.getMessageId(), e);
            return false;
        }
    }
}