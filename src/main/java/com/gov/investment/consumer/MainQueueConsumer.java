package com.gov.investment.consumer;

import com.gov.investment.service.ExternalSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class MainQueueConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ExternalSystemService externalSystemService;

    // 主要业务队列
    @Value("${rabbitmq.queue.main}")
    private String mainQueue;

    // 延迟交换机
    @Value("${rabbitmq.exchange.delay}")
    private String delayExchange;

    // 延迟路由键
    @Value("${rabbitmq.routingkey.delay}")
    private String delayRoutingKey;

    // 死信交换机
    @Value("${rabbitmq.exchange.deadletter}")
    private String deadLetterExchange;

    // 死信路由键
    @Value("${rabbitmq.routingkey.deadletter}")
    private String deadLetterRoutingKey;

    private static final int MAX_RETRY_COUNT = 6;
    private static final long DELAY_TIME = 5000; // 延迟时间5秒

    @RabbitListener(queues = "${rabbitmq.queue.main}")
    public void processMessage(Message message) {
        String messageBody = new String(message.getBody());
        log.info("Received message: {}", messageBody);

        // 获取重试次数
        Integer retryCount = (Integer) message.getMessageProperties().getHeaders().get("x-retry-count");
        if (retryCount == null) {
            retryCount = 0;
        }

        try {
            // 处理业务逻辑
            boolean success = handleBusinessLogic(messageBody);
            if (success) {
                // 业务处理成功，通知外部系统
                externalSystemService.notifyExternalSystem(messageBody);
                log.info("Message processed successfully: {}", messageBody);
            } else {
                // 业务处理失败，重试
                throw new RuntimeException("Business logic processing failed");
            }
        } catch (Exception e) {
            log.error("Error processing message: {}, retry count: {}", messageBody, retryCount, e);
            retryCount++;
            if (retryCount <= MAX_RETRY_COUNT) {
                // 重试次数未达到最大值，发送到延迟队列
                log.info("Retrying message: {}, retry count: {}", messageBody, retryCount);
                rabbitTemplate.convertAndSend(delayExchange, delayRoutingKey, messageBody, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        // 设置延迟时间
                        message.getMessageProperties().setDelay((int) DELAY_TIME);
                        // 设置重试次数
                        message.getMessageProperties().getHeaders().put("x-retry-count", retryCount);
                        return message;
                    }
                });
            } else {
                // 重试次数达到最大值，发送到死信队列
                log.error("Message failed after {} retries, sending to dead letter queue: {}", MAX_RETRY_COUNT, messageBody);
                rabbitTemplate.convertAndSend(deadLetterExchange, deadLetterRoutingKey, messageBody);
            }
        }
    }

    /**
     * 处理业务逻辑
     * @param messageBody 消息内容
     * @return 处理结果
     */
    private boolean handleBusinessLogic(String messageBody) {
        // 这里实现具体的业务逻辑
        // 例如：处理投资相关的业务
        log.info("Processing business logic for message: {}", messageBody);
        // 模拟业务处理失败的情况
        // return false;
        // 模拟业务处理成功的情况
        return true;
    }
}