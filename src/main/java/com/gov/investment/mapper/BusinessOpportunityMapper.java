package com.gov.investment.mapper;

import com.gov.investment.entity.BusinessOpportunity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BusinessOpportunityMapper {
    
    List<BusinessOpportunity> selectAllBusinessOpportunities();
    
    BusinessOpportunity selectBusinessOpportunityById(@Param("id") Long id);
    
    void insert(BusinessOpportunity businessOpportunity);
    
    void update(BusinessOpportunity businessOpportunity);
    
    void delete(@Param("id") Long id);
}