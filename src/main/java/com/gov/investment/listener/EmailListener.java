package com.gov.investment.listener;

import com.gov.investment.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailListener {
    
    @Autowired
    private EmailService emailService;
    
    @JmsListener(destination = "emailQueue")
    public void onMessage(Map<String, Object> message) {
        String to = (String) message.get("to");
        String subject = (String) message.get("subject");
        String content = (String) message.get("content");
        emailService.sendEmail(to, subject, content);
    }
}
