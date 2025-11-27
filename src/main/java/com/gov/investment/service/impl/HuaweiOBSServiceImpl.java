package com.gov.investment.service.impl;

import com.gov.investment.service.HuaweiOBSService;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HuaweiOBSServiceImpl implements HuaweiOBSService {

    private static final Logger logger = LoggerFactory.getLogger(HuaweiOBSServiceImpl.class);

    @Autowired
    private ObsClient obsClient;

    @Value("${huawei.obs.bucket-name}")
    private String bucketName;

    @Override
    public boolean uploadToOBS(String messageId, String content) {
        try {
            // 生成OBS中的文件路径，格式：dead-letter/yyyyMMdd/消息ID.txt
            String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String objectKey = String.format("dead-letter/%s/%s.txt", dateStr, messageId);

            // 创建PutObjectRequest对象
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(objectKey);
            request.setInput(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));

            // 上传文件到OBS
            PutObjectResult result = obsClient.putObject(request);

            logger.info("消息上传到华为云OBS成功，消息ID: {}, OBS地址: {}", messageId, objectKey);
            return true;
        } catch (ObsException e) {
            logger.error("消息上传到华为云OBS失败，消息ID: {}", messageId, e);
            return false;
        } catch (Exception e) {
            logger.error("消息上传到华为云OBS时发生未知错误，消息ID: {}", messageId, e);
            return false;
        }
    }
}