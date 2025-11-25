package com.gov.investment.service;

import com.gov.investment.model.BusinessOpportunity;

import java.util.List;

public interface BusinessOpportunityService {
    boolean addBusinessOpportunity(BusinessOpportunity businessOpportunity);
    boolean updateBusinessOpportunity(BusinessOpportunity businessOpportunity);
    boolean deleteBusinessOpportunity(Long id);
    BusinessOpportunity getBusinessOpportunityById(Long id);
    List<BusinessOpportunity> getAllBusinessOpportunities();
    List<BusinessOpportunity> getBusinessOpportunitiesByCondition(String name, String type, String status, Long supplierId);
    boolean validateBusinessOpportunity(BusinessOpportunity businessOpportunity);
}