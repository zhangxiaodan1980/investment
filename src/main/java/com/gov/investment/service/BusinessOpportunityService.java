package com.gov.investment.service;

import com.gov.investment.model.BusinessOpportunity;

import java.util.List;

public interface BusinessOpportunityService {

    void save(BusinessOpportunity businessOpportunity);

    void update(BusinessOpportunity businessOpportunity);

    void delete(BigDecimal id);

    BusinessOpportunity findById(BigDecimal id);

    List<BusinessOpportunity> findByName(String name);

    List<BusinessOpportunity> findBySupplierId(BigDecimal supplierId);

    List<BusinessOpportunity> findAll();

    boolean validateBusinessOpportunity(BusinessOpportunity businessOpportunity);
}
