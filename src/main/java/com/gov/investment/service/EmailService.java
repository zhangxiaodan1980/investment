package com.gov.investment.service;

public interface EmailService {
    
    void sendEmail(String to, String subject, String content);
}
