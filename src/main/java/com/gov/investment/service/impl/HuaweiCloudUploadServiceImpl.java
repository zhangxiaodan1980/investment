package com.gov.investment.service.impl;

import com.gov.investment.service.HuaweiCloudUploadService;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Slf4j
public class HuaweiCloudUploadServiceImpl implements HuaweiCloudUploadService {
    
    @Value("${huawei.cloud.obs.endpoint}")
    private String endpoint;
    
    @Value("${huawei.cloud.obs.access-key}")
    private String accessKey;
    
    @Value("${huawei.cloud.obs.secret-key}")
    private String secretKey;
    
    @Value("${huawei.cloud.obs.bucket-name}")
    private String bucketName;
    
    @Override
    public void uploadToHuaweiCloud(String messageBody) {
        ObsClient obsClient = null;
        try {
            // Initialize OBS client
            obsClient = new ObsClient(accessKey, secretKey, endpoint);
            
            // Create input stream from message body
            InputStream inputStream = new ByteArrayInputStream(messageBody.getBytes());
            
            // Generate a unique object key
            String objectKey = "message-" + System.currentTimeMillis() + ".txt";
            
            // Upload object to OBS
            PutObjectResult result = obsClient.putObject(bucketName, objectKey, inputStream);
            
            log.info("Uploaded to Huawei Cloud OBS successfully. ETag: {}, Object Key: {}", result.getEtag(), objectKey);
        } catch (Exception e) {
            log.error("Failed to upload to Huawei Cloud OBS", e);
            // Handle exception as needed
        } finally {
            // Close OBS client
            if (obsClient != null) {
                try {
                    obsClient.close();
                } catch (Exception e) {
                    log.error("Failed to close OBS client", e);
                }
            }
        }
    }
}
