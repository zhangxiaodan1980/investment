package com.gov.investment.controller;

import com.gov.investment.service.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageProducer messageProducer;

    /**
     * 发送消息到业务队列
     * @param content 消息内容
     * @return 响应结果
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody String content) {
        Map<String, Object> response = new HashMap<>();
        try {
            String messageId = messageProducer.sendMessageToBusinessQueue(content);
            response.put("success", true);
            response.put("messageId", messageId);
            response.put("message", "消息发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "消息发送失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 发送延迟消息
     * @param content 消息内容
     * @param delayTime 延迟时间（毫秒）
     * @return 响应结果
     */
    @PostMapping("/send-delay")
    public ResponseEntity<Map<String, Object>> sendDelayMessage(
            @RequestBody String content,
            @RequestParam(defaultValue = "5000") long delayTime) {
        Map<String, Object> response = new HashMap<>();
        try {
            String messageId = messageProducer.sendMessageToDelayQueue(content, delayTime, 0);
            response.put("success", true);
            response.put("messageId", messageId);
            response.put("delayTime", delayTime);
            response.put("message", "延迟消息发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "延迟消息发送失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 发送消息到死信队列
     * @param content 消息内容
     * @param retryCount 重试次数
     * @return 响应结果
     */
    @PostMapping("/send-dead-letter")
    public ResponseEntity<Map<String, Object>> sendDeadLetterMessage(
            @RequestBody String content,
            @RequestParam(defaultValue = "0") int retryCount) {
        Map<String, Object> response = new HashMap<>();
        try {
            String messageId = messageProducer.sendMessageToDeadLetterQueue(content, retryCount);
            response.put("success", true);
            response.put("messageId", messageId);
            response.put("retryCount", retryCount);
            response.put("message", "死信消息发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "死信消息发送失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}