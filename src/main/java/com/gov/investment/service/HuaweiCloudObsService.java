package com.gov.investment.service;

public interface HuaweiCloudObsService {

    /**
     * 将消息上传到华为云OBS
     * @param messageBody 消息内容
     * @return 上传结果
     */
    boolean uploadToObs(String messageBody);
}