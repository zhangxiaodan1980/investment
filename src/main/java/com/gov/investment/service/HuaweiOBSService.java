package com.gov.investment.service;

public interface HuaweiOBSService {
    /**
     * 上传消息到华为云OBS
     * @param messageId 消息ID
     * @param content 消息内容
     * @return 上传是否成功
     */
    boolean uploadToOBS(String messageId, String content);
}