package com.gov.investment.service.impl;

import com.gov.investment.service.ExternalSystemNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalSystemNotificationServiceImpl implements ExternalSystemNotificationService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${external.system.url}")
    private String externalSystemUrl;
    
    @Override
    public void notifyExternalSystem(String messageBody) {
        try {
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Create request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(messageBody, headers);
            
            // Call external system API
            ResponseEntity<String> response = restTemplate.exchange(
                    externalSystemUrl, 
                    HttpMethod.POST, 
                    requestEntity, 
                    String.class
            );
            
            log.info("Notified external system successfully. Response: {}", response.getBody());
        } catch (Exception e) {
            log.error("Failed to notify external system", e);
            // Handle exception as needed
        }
    }
}
