package com.gov.investment.config;

import com.gov.investment.util.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {
    
    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        // 这里可以根据实际情况配置workerId和dataCenterId
        // 例如从配置文件中读取或者使用环境变量
        long workerId = 1L;
        long dataCenterId = 1L;
        return new SnowflakeIdWorker(workerId, dataCenterId);
    }
}
