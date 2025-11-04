package com.gov.investment.service.impl;

import com.gov.investment.mapper.BusinessOpportunityMapper;
import com.gov.investment.model.BusinessOpportunity;
import com.gov.investment.service.BusinessOpportunityService;
import com.gov.investment.service.ProjectService;
import com.gov.investment.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessOpportunityServiceImpl implements BusinessOpportunityService {
    
    @Autowired
    private BusinessOpportunityMapper businessOpportunityMapper;
    
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    // 外部公司验证API地址
    private static final String COMPANY_VERIFICATION_API = "https://api.example.com/verify-company";
    
    // 邮件队列名称
    private static final String EMAIL_QUEUE = "emailQueue";
    
    @Override
    public int createBusinessOpportunity(BusinessOpportunity businessOpportunity) {
        // 验证关联项目是否存在
        if (projectService.getProjectById(businessOpportunity.getProject().getId()) == null) {
            throw new RuntimeException("关联项目不存在");
        }
        
        // 验证公司和法人信息
        if (!verifyCompanyInfo(businessOpportunity.getCompanyName(), businessOpportunity.getLegalPerson())) {
            throw new RuntimeException("公司或法人信息验证失败");
        }
        
        // 生成ID和设置时间戳
        businessOpportunity.setId(snowflakeIdWorker.nextBigDecimalId());
        businessOpportunity.setCreateTime(new Date());
        businessOpportunity.setUpdateTime(new Date());
        
        // 保存到数据库
        int result = businessOpportunityMapper.insertBusinessOpportunity(businessOpportunity);
        
        // 发送邮件通知
        sendEmailNotification(businessOpportunity);
        
        return result;
    }
    
    @Override
    public BusinessOpportunity getBusinessOpportunityById(BigDecimal id) {
        return businessOpportunityMapper.selectBusinessOpportunityById(id);
    }
    
    @Override
    public int updateBusinessOpportunity(BusinessOpportunity businessOpportunity) {
        // 验证商机是否存在
        if (getBusinessOpportunityById(businessOpportunity.getId()) == null) {
            throw new RuntimeException("商机不存在");
        }
        
        // 验证关联项目是否存在
        if (projectService.getProjectById(businessOpportunity.getProject().getId()) == null) {
            throw new RuntimeException("关联项目不存在");
        }
        
        // 验证公司和法人信息（如果有变化）
        BusinessOpportunity existingOpportunity = getBusinessOpportunityById(businessOpportunity.getId());
        if (!existingOpportunity.getCompanyName().equals(businessOpportunity.getCompanyName()) ||
            !existingOpportunity.getLegalPerson().equals(businessOpportunity.getLegalPerson())) {
            if (!verifyCompanyInfo(businessOpportunity.getCompanyName(), businessOpportunity.getLegalPerson())) {
                throw new RuntimeException("公司或法人信息验证失败");
            }
        }
        
        // 更新时间戳
        businessOpportunity.setUpdateTime(new Date());
        
        // 保存到数据库
        return businessOpportunityMapper.updateBusinessOpportunity(businessOpportunity);
    }
    
    @Override
    public int deleteBusinessOpportunity(BigDecimal id) {
        return businessOpportunityMapper.deleteBusinessOpportunityById(id);
    }
    
    @Override
    public List<BusinessOpportunity> getAllBusinessOpportunities() {
        return businessOpportunityMapper.selectAllBusinessOpportunities();
    }
    
    @Override
    public List<BusinessOpportunity> getBusinessOpportunitiesByProjectId(BigDecimal projectId) {
        return businessOpportunityMapper.selectBusinessOpportunitiesByProjectId(projectId);
    }
    
    /**
     * 调用外部API验证公司和法人信息
     */
    private boolean verifyCompanyInfo(String companyName, String legalPerson) {
        Map<String, String> params = new HashMap<>();
        params.put("companyName", companyName);
        params.put("legalPerson", legalPerson);
        
        try {
            // 调用外部API
            Map<String, Object> response = restTemplate.getForObject(COMPANY_VERIFICATION_API, Map.class, params);
            // 假设API返回一个"valid"字段表示验证结果
            return response != null && Boolean.TRUE.equals(response.get("valid"));
        } catch (Exception e) {
            // 处理API调用失败，记录日志
            e.printStackTrace();
            // 这里为了演示方便，如果API调用失败则默认验证通过
            return true;
        }
    }
    
    /**
     * 通过消息队列发送邮件通知
     */
    private void sendEmailNotification(BusinessOpportunity businessOpportunity) {
        // 构造邮件消息
        Map<String, Object> emailMessage = new HashMap<>();
        emailMessage.put("to", "admin@example.com");
        emailMessage.put("subject", "新商机创建通知");
        emailMessage.put("content", String.format("新的商机已创建：\n" +
                "商机名称：%s\n" +
                "公司名称：%s\n" +
                "法人姓名：%s\n" +
                "所属项目：%s",
                businessOpportunity.getOpportunityName(),
                businessOpportunity.getCompanyName(),
                businessOpportunity.getLegalPerson(),
                businessOpportunity.getProject().getProjectName()));
        
        // 发送消息到邮件队列
        jmsTemplate.convertAndSend(EMAIL_QUEUE, emailMessage);
    }
}
