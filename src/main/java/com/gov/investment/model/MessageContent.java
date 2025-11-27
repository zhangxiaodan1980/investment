package com.gov.investment.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageContent implements Serializable {
    private static final long serialVersionUID = 1L;

    // 消息ID
    private String messageId;

    // 消息内容
    private String content;

    // 重试次数
    private int retryCount = 0;

    // 创建时间
    private long createTime;

    // 最后一次处理时间
    private long lastProcessTime;
}