package com.gov.investment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 创建并配置RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        
        // 这里可以添加拦截器、消息转换器等配置
        // 例如：添加日志拦截器
        // restTemplate.getInterceptors().add(new LoggingInterceptor());
        
        return restTemplate;
    }
}