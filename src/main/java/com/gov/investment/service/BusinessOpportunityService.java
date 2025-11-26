package com.gov.investment.service;

import com.gov.investment.entity.BusinessOpportunity;

import java.util.List;

public interface BusinessOpportunityService {
    
    List<BusinessOpportunity> getAllBusinessOpportunities();
    
    BusinessOpportunity getBusinessOpportunityById(Long id);
    
    void createBusinessOpportunity(BusinessOpportunity businessOpportunity);
    
    void updateBusinessOpportunity(BusinessOpportunity businessOpportunity);
    
    void deleteBusinessOpportunity(Long id);
}