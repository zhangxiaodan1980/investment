package com.gov.investment.service;

public interface ExternalSystemService {

    /**
     * 通知外部系统
     * @param messageBody 消息内容
     */
    void notifyExternalSystem(String messageBody);
}