package com.gov.investment.service;

import com.gov.investment.model.Supplier;

import java.util.List;

public interface SupplierService {
    boolean addSupplier(Supplier supplier);
    boolean updateSupplier(Supplier supplier);
    boolean deleteSupplier(Long id);
    Supplier getSupplierById(Long id);
    List<Supplier> getAllSuppliers();
    List<Supplier> getSuppliersByCondition(String name, String type, String status);
    boolean validateSupplier(Supplier supplier);
}