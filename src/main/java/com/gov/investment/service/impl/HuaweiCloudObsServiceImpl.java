package com.gov.investment.service.impl;

import com.gov.investment.service.HuaweiCloudObsService;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class HuaweiCloudObsServiceImpl implements HuaweiCloudObsService {

    @Value("${huaweicloud.obs.endpoint}")
    private String endpoint;

    @Value("${huaweicloud.obs.accessKey}")
    private String accessKey;

    @Value("${huaweicloud.obs.secretKey}")
    private String secretKey;

    @Value("${huaweicloud.obs.bucketName}")
    private String bucketName;

    @Override
    public boolean uploadToObs(String messageBody) {
        ObsClient obsClient = null;
        try {
            // 创建ObsClient实例
            obsClient = new ObsClient(accessKey, secretKey, endpoint);

            // 生成唯一的文件名
            String fileName = generateFileName();

            // 将消息内容转换为输入流
            InputStream inputStream = new ByteArrayInputStream(messageBody.getBytes());

            // 创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest();
            putObjectRequest.setBucketName(bucketName);
            putObjectRequest.setObjectKey(fileName);
            putObjectRequest.setInput(inputStream);

            // 上传文件
            PutObjectResult putObjectResult = obsClient.putObject(putObjectRequest);

            log.info("Successfully uploaded message to OBS, bucket: {}, object key: {}, etag: {}",
                    bucketName, fileName, putObjectResult.getEtag());

            return true;
        } catch (ObsException e) {
            log.error("Error uploading message to OBS: {}", messageBody, e);
            log.error("Error code: {}, error message: {}", e.getErrorCode(), e.getErrorMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error uploading message to OBS: {}", messageBody, e);
            return false;
        } finally {
            // 关闭ObsClient
            if (obsClient != null) {
                try {
                    obsClient.close();
                } catch (Exception e) {
                    log.error("Error closing ObsClient", e);
                }
            }
        }
    }

    /**
     * 生成唯一的文件名
     * @return 文件名
     */
    private String generateFileName() {
        // 格式：yyyyMMddHHmmss-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String uuid = UUID.randomUUID().toString();
        return timestamp + "-" + uuid + ".txt";
    }
}