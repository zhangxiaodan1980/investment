package com.gov.investment.consumer;

import com.gov.investment.service.HuaweiCloudObsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeadLetterQueueConsumer {

    @Autowired
    private HuaweiCloudObsService huaweiCloudObsService;

    @RabbitListener(queues = "${rabbitmq.queue.deadletter}")
    public void processDeadLetterMessage(String messageBody) {
        log.info("Received dead letter message: {}", messageBody);

        try {
            // 将死信消息上传到华为云OBS
            boolean success = huaweiCloudObsService.uploadToObs(messageBody);
            if (success) {
                log.info("Dead letter message uploaded to Huawei Cloud OBS successfully: {}", messageBody);
            } else {
                log.error("Failed to upload dead letter message to Huawei Cloud OBS: {}", messageBody);
            }
        } catch (Exception e) {
            log.error("Error processing dead letter message: {}", messageBody, e);
        }
    }
}