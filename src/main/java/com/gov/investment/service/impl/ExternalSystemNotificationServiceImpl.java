package com.gov.investment.service.impl;

import com.gov.investment.service.ExternalSystemNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalSystemNotificationServiceImpl implements ExternalSystemNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalSystemNotificationServiceImpl.class);

    // 外部系统通知URL（从配置文件中读取）
    @Value("${external.system.notification.url}")
    private String externalSystemUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean notifyExternalSystem(String messageId, String content) {
        try {
            // 创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // 创建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messageId", messageId);
            requestBody.put("content", content);
            requestBody.put("status", "success");
            requestBody.put("timestamp", System.currentTimeMillis());

            // 创建HttpEntity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 构建请求URL（如果需要动态参数）
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(externalSystemUrl);
            // 如果需要添加查询参数，可以使用builder.queryParam("paramName", paramValue);

            // 发送POST请求
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 检查响应状态
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("通知外部系统成功，消息ID: {}, 响应: {}", messageId, response.getBody());
                return true;
            } else {
                logger.error("通知外部系统失败，消息ID: {}, 状态码: {}, 响应: {}",
                        messageId, response.getStatusCode(), response.getBody());
                return false;
            }
        } catch (Exception e) {
            logger.error("通知外部系统失败，消息ID: {}", messageId, e);
            return false;
        }
    }
}