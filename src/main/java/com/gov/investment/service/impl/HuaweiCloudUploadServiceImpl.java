package com.gov.investment.service.impl;

import com.gov.investment.service.HuaweiCloudUploadService;
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
public class HuaweiCloudUploadServiceImpl implements HuaweiCloudUploadService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${huawei.cloud.obs.url}")
    private String obsApiUrl;
    
    @Value("${huawei.cloud.access.token}")
    private String accessToken;
    
    @Override
    public void uploadToHuaweiCloud(String messageBody) {
        try {
            // Set headers (need to include authentication headers)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // Add Huawei Cloud authentication headers here
            headers.set("Authorization", "Bearer " + accessToken);
            
            // Create request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(messageBody, headers);
            
            // Call Huawei Cloud OBS API
            ResponseEntity<String> response = restTemplate.exchange(
                    obsApiUrl, 
                    HttpMethod.PUT, 
                    requestEntity, 
                    String.class
            );
            
            log.info("Uploaded to Huawei Cloud successfully. Status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Failed to upload to Huawei Cloud", e);
            // Handle exception as needed
        }
    }
}
