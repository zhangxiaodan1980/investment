package com.gov.investment.mapper;

import com.gov.investment.model.BusinessOpportunity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BusinessOpportunityMapper {

    @Insert("INSERT INTO business_opportunity (name, description, supplier_id, status, estimated_amount, expected_close_time, remark, create_time, update_time) " +
            "VALUES (#{name}, #{description}, #{supplierId}, #{status}, #{estimatedAmount}, #{expectedCloseTime}, #{remark}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(BusinessOpportunity businessOpportunity);

    @Update("UPDATE business_opportunity SET name = #{name}, description = #{description}, supplier_id = #{supplierId}, status = #{status}, " +
            "estimated_amount = #{estimatedAmount}, expected_close_time = #{expectedCloseTime}, remark = #{remark}, update_time = #{updateTime} WHERE id = #{id}")
    void update(BusinessOpportunity businessOpportunity);

    @Delete("DELETE FROM business_opportunity WHERE id = #{id}")
    void delete(BigDecimal id);

    @Select("SELECT * FROM business_opportunity WHERE id = #{id}")
    BusinessOpportunity findById(BigDecimal id);

    @Select("SELECT * FROM business_opportunity WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<BusinessOpportunity> findByName(String name);

    @Select("SELECT * FROM business_opportunity WHERE supplier_id = #{supplierId}")
    List<BusinessOpportunity> findBySupplierId(BigDecimal supplierId);

    @Select("SELECT * FROM business_opportunity")
    List<BusinessOpportunity> findAll();
}