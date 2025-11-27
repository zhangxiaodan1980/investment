package com.gov.investment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gov.investment.config.RabbitMQConfig;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.UUID;

@Service
public class MessageService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ObsClient obsClient;
    private final String bucketName;

    public MessageService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper,
                          @Value("${obs.endpoint}") String endpoint,
                          @Value("${obs.accessKeyId}") String accessKeyId,
                          @Value("${obs.secretAccessKey}") String secretAccessKey,
                          @Value("${obs.bucketName}") String bucketName) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
        this.obsClient = new ObsClient(accessKeyId, secretAccessKey, endpoint);
        this.bucketName = bucketName;
    }

    /**
     * Process message
     * @param message Message content
     */
    public void processMessage(String message) {
        try {
            // Parse JSON message into Customer object
            Customer customer = objectMapper.readValue(message, Customer.class);
            System.out.println("Processing customer: " + customer.getName());

            // In a real-world scenario, you would validate the customer and store it in a database
            // For example, you could call a repository to save the customer
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing message: " + e.getMessage());
            // Handle parsing error, maybe send to dead-letter queue
        }
    }

    /**
     * Notify external system
     * @param message Message content
     */
    public void notifyExternalSystem(String message) {
        try {
            // Parse JSON message into Customer object
            Customer customer = objectMapper.readValue(message, Customer.class);

            // Prepare HTTP request to external API
            String externalApiUrl = "http://example.com/api/customers";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HTTP entity with customer object and headers
            HttpEntity<Customer> request = new HttpEntity<>(customer, headers);

            // Send POST request to external API
            restTemplate.postForObject(externalApiUrl, request, String.class);
            System.out.println("Successfully notified external system for customer: " + customer.getName());
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing message: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error notifying external system: " + e.getMessage());
            // Handle notification error, maybe retry
        }
    }

    /**
     * Upload to Huawei Cloud OBS
     * @param message Message content
     */
    public void uploadToHuaweiCloud(String message) {
        try {
            // Generate a unique filename using UUID
            String fileName = "dead-letter-" + UUID.randomUUID().toString() + ".txt";
            
            // Convert message to byte array input stream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(message.getBytes("UTF-8"));
            
            // Create put object request
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream);
            
            // Upload the message to OBS
            obsClient.putObject(request);
            
            System.out.println("Successfully uploaded message to Huawei Cloud OBS: " + fileName);
        } catch (Exception e) {
            System.err.println("Error uploading message to Huawei Cloud OBS: " + e.getMessage());
        }
    }

    // Customer class to represent message structure
    private static class Customer {
        private String id;
        private String name;
        private String email;

        // Getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    /**
     * Handle message failure
     * @param message Original message
     */
    public void handleMessageFailure(Message message) {
        // Get current retry count
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        int retryCount = headers.getOrDefault("x-retry-count", 0) instanceof Integer ?
                (int) headers.getOrDefault("x-retry-count", 0) : 0;
        
        retryCount++;
        
        // Update retry count header
        message.getMessageProperties().getHeaders().put("x-retry-count", retryCount);
        
        try {
            // Convert message to string
            String messageContent = new String(message.getBody(), "UTF-8");
            
            if (retryCount < RabbitMQConfig.MAX_RETRY_ATTEMPTS) {
                // Send to delay queue
                rabbitTemplate.convertAndSend(RabbitMQConfig.DELAY_EXCHANGE,
                        RabbitMQConfig.DELAY_ROUTING_KEY,
                        messageContent,
                        msg -> {
                            msg.getMessageProperties().getHeaders().put("x-retry-count", retryCount);
                            return msg;
                        });
                System.out.println("Message sent to delay queue, retry count: " + retryCount);
            } else {
                // Send to dead-letter queue
                rabbitTemplate.convertAndSend(RabbitMQConfig.DEAD_LETTER_EXCHANGE,
                        RabbitMQConfig.DEAD_LETTER_ROUTING_KEY,
                        messageContent);
                System.out.println("Message sent to dead-letter queue, retry count: " + retryCount);
                
                // Upload to Huawei Cloud
                uploadToHuaweiCloud(messageContent);
            }
        } catch (Exception e) {
            System.err.println("Error handling message failure: " + e.getMessage());
        }
    }
}
