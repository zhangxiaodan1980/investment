package com.gov.investment.mapper;

import com.gov.investment.model.BusinessOpportunity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BusinessOpportunityMapper {
    int insert(BusinessOpportunity businessOpportunity);
    int update(BusinessOpportunity businessOpportunity);
    int deleteById(@Param("id") Long id);
    BusinessOpportunity selectById(@Param("id") Long id);
    List<BusinessOpportunity> selectAll();
    List<BusinessOpportunity> selectByCondition(@Param("name") String name, @Param("type") String type, @Param("status") String status, @Param("supplierId") Long supplierId);
}