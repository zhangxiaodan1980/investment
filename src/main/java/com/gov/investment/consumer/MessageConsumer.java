package com.gov.investment.consumer;

import com.gov.investment.service.ExternalSystemNotificationService;
import com.gov.investment.service.HuaweiCloudUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ExternalSystemNotificationService externalSystemNotificationService;

    @Autowired
    private HuaweiCloudUploadService huaweiCloudUploadService;

    private static final int MAX_RETRIES = 6;

    @RabbitListener(queues = "${rabbitmq.queue.normal}")
    public void handleMessage(Message message) {
        try {
            String messageBody = new String(message.getBody());
            log.info("Received message: {}", messageBody);

            // Simulate message processing
            processMessage(messageBody);

            // Notify external system on success
            externalSystemNotificationService.notifyExternalSystem(messageBody);
            log.info("Message processed successfully and external system notified");
        } catch (Exception e) {
            log.error("Error processing message", e);

            // Get retry count from message headers
            Integer retryCount = (Integer) message.getMessageProperties().getHeaders().getOrDefault("retry_count", 0);
            retryCount++;

            if (retryCount < MAX_RETRIES) {
                // Republish to normal queue with increased retry count
                message.getMessageProperties().getHeaders().put("retry_count", retryCount);
                rabbitTemplate.send(message.getMessageProperties().getReceivedExchange(),
                        message.getMessageProperties().getReceivedRoutingKey(),
                        message);
                log.info("Message republished to normal queue, retry count: {}", retryCount);
            } else {
                // Publish to dead letter queue after max retries
                log.error("Max retries reached, publishing message to dead letter queue");
                // The message will be automatically routed to DLQ via RabbitMQ dead letter exchange configuration
                // So we can just throw an exception to trigger the dead letter routing
                throw new RuntimeException("Max retries reached for message");
            }
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.dlq}")
    public void handleDeadLetterMessage(Message message) {
        try {
            String messageBody = new String(message.getBody());
            log.info("Received dead letter message: {}", messageBody);

            // Upload to Huawei Cloud
            huaweiCloudUploadService.uploadToHuaweiCloud(messageBody);
            log.info("Dead letter message uploaded to Huawei Cloud successfully");
        } catch (Exception e) {
            log.error("Error handling dead letter message", e);
            // Handle error for Huawei Cloud upload if needed
        }
    }

    private static int counter = 0;

    private void processMessage(String messageBody) throws Exception {
        // Replace with actual message processing logic
        // For simulation, let's throw an exception 5 times to test retry mechanism
        if (counter < 5) {
            counter++;
            throw new RuntimeException("Simulated error processing message");
        }
    }
}
