package com.gov.investment.mapper;

import com.gov.investment.model.BusinessOpportunity;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BusinessOpportunityMapper {
    
    @Insert("INSERT INTO business_opportunity (id, opportunity_name, opportunity_code, company_name, legal_person, " +
            "company_address, contact_person, contact_phone, amount, status, create_time, update_time, project_id) " +
            "VALUES (#{id}, #{opportunityName}, #{opportunityCode}, #{companyName}, #{legalPerson}, #{companyAddress}, " +
            "#{contactPerson}, #{contactPhone}, #{amount}, #{status}, #{createTime}, #{updateTime}, #{project.id})")
    int insertBusinessOpportunity(BusinessOpportunity businessOpportunity);
    
    @Select("SELECT * FROM business_opportunity WHERE id = #{id}")
    BusinessOpportunity selectBusinessOpportunityById(BigDecimal id);
    
    @Update("UPDATE business_opportunity SET opportunity_name = #{opportunityName}, opportunity_code = #{opportunityCode}, " +
            "company_name = #{companyName}, legal_person = #{legalPerson}, company_address = #{companyAddress}, " +
            "contact_person = #{contactPerson}, contact_phone = #{contactPhone}, amount = #{amount}, " +
            "status = #{status}, update_time = #{updateTime}, project_id = #{project.id} WHERE id = #{id}")
    int updateBusinessOpportunity(BusinessOpportunity businessOpportunity);
    
    @Delete("DELETE FROM business_opportunity WHERE id = #{id}")
    int deleteBusinessOpportunityById(BigDecimal id);
    
    @Select("SELECT * FROM business_opportunity")
    List<BusinessOpportunity> selectAllBusinessOpportunities();
    
    @Select("SELECT * FROM business_opportunity WHERE project_id = #{projectId}")
    List<BusinessOpportunity> selectBusinessOpportunitiesByProjectId(BigDecimal projectId);
}
