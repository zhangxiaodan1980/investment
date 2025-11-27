package com.gov.investment.service.impl;

import com.gov.investment.service.ExternalSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ExternalSystemServiceImpl implements ExternalSystemService {

    @Autowired
    private RestTemplate restTemplate;

    // 外部系统API地址
    private static final String EXTERNAL_SYSTEM_API_URL = "http://external-system/api/notify";

    @Override
    public void notifyExternalSystem(String messageBody) {
        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 创建请求实体
            HttpEntity<String> requestEntity = new HttpEntity<>(messageBody, headers);

            // 调用外部系统API
            ResponseEntity<String> response = restTemplate.postForEntity(EXTERNAL_SYSTEM_API_URL, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully notified external system: {}", messageBody);
            } else {
                log.error("Failed to notify external system, status code: {}, message: {}", response.getStatusCode(), messageBody);
            }
        } catch (Exception e) {
            log.error("Error notifying external system: {}", messageBody, e);
        }
    }
}