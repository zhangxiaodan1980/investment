package com.gov.investment.mapper;

import com.gov.investment.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierMapper {
    
    List<Supplier> selectAllSuppliers();
    
    Supplier selectSupplierById(@Param("id") Long id);
    
    void insert(Supplier supplier);
    
    void update(Supplier supplier);
    
    void delete(@Param("id") Long id);
}