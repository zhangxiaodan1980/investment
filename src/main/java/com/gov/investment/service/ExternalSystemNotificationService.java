package com.gov.investment.service;

public interface ExternalSystemNotificationService {
    /**
     * 通知外部系统消息处理成功
     * @param messageId 消息ID
     * @param content 消息内容
     * @return 通知是否成功
     */
    boolean notifyExternalSystem(String messageId, String content);
}