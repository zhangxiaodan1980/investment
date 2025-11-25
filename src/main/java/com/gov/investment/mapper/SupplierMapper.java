package com.gov.investment.mapper;

import com.gov.investment.model.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierMapper {
    int insert(Supplier supplier);
    int update(Supplier supplier);
    int deleteById(@Param("id") Long id);
    Supplier selectById(@Param("id") Long id);
    List<Supplier> selectAll();
    List<Supplier> selectByCondition(@Param("name") String name, @Param("type") String type, @Param("status") String status);
}