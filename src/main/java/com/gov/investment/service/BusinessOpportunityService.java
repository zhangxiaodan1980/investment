package com.gov.investment.service;

import com.gov.investment.model.BusinessOpportunity;
import java.math.BigDecimal;
import java.util.List;

public interface BusinessOpportunityService {
    
    int createBusinessOpportunity(BusinessOpportunity businessOpportunity);
    
    BusinessOpportunity getBusinessOpportunityById(BigDecimal id);
    
    int updateBusinessOpportunity(BusinessOpportunity businessOpportunity);
    
    int deleteBusinessOpportunity(BigDecimal id);
    
    List<BusinessOpportunity> getAllBusinessOpportunities();
    
    List<BusinessOpportunity> getBusinessOpportunitiesByProjectId(BigDecimal projectId);
}
