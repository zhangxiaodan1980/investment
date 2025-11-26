package com.gov.investment.service.impl;

import com.gov.investment.entity.BusinessOpportunity;
import com.gov.investment.mapper.BusinessOpportunityMapper;
import com.gov.investment.service.BusinessOpportunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BusinessOpportunityServiceImpl implements BusinessOpportunityService {
    
    @Autowired
    private BusinessOpportunityMapper businessOpportunityMapper;
    
    @Override
    public List<BusinessOpportunity> getAllBusinessOpportunities() {
        return businessOpportunityMapper.selectAllBusinessOpportunities();
    }
    
    @Override
    public BusinessOpportunity getBusinessOpportunityById(Long id) {
        return businessOpportunityMapper.selectBusinessOpportunityById(id);
    }
    
    @Override
    public void createBusinessOpportunity(BusinessOpportunity businessOpportunity) {
        businessOpportunityMapper.insert(businessOpportunity);
    }
    
    @Override
    public void updateBusinessOpportunity(BusinessOpportunity businessOpportunity) {
        businessOpportunityMapper.update(businessOpportunity);
    }
    
    @Override
    public void deleteBusinessOpportunity(Long id) {
        businessOpportunityMapper.delete(id);
    }
}