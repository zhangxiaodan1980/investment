package com.gov.investment.consumer;

import com.gov.investment.config.RabbitMQConfig;
import com.gov.investment.service.MessageService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class MessageConsumer implements ChannelAwareMessageListener {

    private final MessageService messageService;

    public MessageConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.MAIN_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    @RabbitListener(queues = RabbitMQConfig.DELAY_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    @RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message, Channel channel) throws Exception {
        String queueName = message.getMessageProperties().getConsumerQueue();
        String messageContent = new String(message.getBody(), "UTF-8");
        
        try {
            switch (queueName) {
                case RabbitMQConfig.MAIN_QUEUE:
                case RabbitMQConfig.DELAY_QUEUE:
                    // Process message
                    messageService.processMessage(messageContent);
                    
                    // If processing is successful, notify external system
                    messageService.notifyExternalSystem(messageContent);
                    
                    // Acknowledge message
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    System.out.println("Message processed successfully: " + messageContent);
                    break;
                    
                case RabbitMQConfig.DEAD_LETTER_QUEUE:
                    // Handle dead-letter message (already uploaded to Huawei Cloud)
                    System.out.println("Received dead-letter message: " + messageContent);
                    
                    // Acknowledge message
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    break;
                    
                default:
                    System.err.println("Unknown queue: " + queueName);
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            
            if (queueName.equals(RabbitMQConfig.MAIN_QUEUE) || queueName.equals(RabbitMQConfig.DELAY_QUEUE)) {
                // Handle message failure
                messageService.handleMessageFailure(message);
                
                // Reject message (don't requeue)
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } else {
                // For dead-letter queue, just acknowledge
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        }
    }
}
