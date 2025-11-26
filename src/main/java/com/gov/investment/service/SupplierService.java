package com.gov.investment.service;

import com.gov.investment.entity.Supplier;

import java.util.List;

public interface SupplierService {
    
    List<Supplier> getAllSuppliers();
    
    Supplier getSupplierById(Long id);
    
    void createSupplier(Supplier supplier);
    
    void updateSupplier(Supplier supplier);
    
    void deleteSupplier(Long id);
}