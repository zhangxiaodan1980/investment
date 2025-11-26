package com.gov.investment.service.impl;

import com.gov.investment.entity.Supplier;
import com.gov.investment.mapper.SupplierMapper;
import com.gov.investment.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    
    @Autowired
    private SupplierMapper supplierMapper;
    
    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierMapper.selectAllSuppliers();
    }
    
    @Override
    public Supplier getSupplierById(Long id) {
        return supplierMapper.selectSupplierById(id);
    }
    
    @Override
    public void createSupplier(Supplier supplier) {
        supplierMapper.insert(supplier);
    }
    
    @Override
    public void updateSupplier(Supplier supplier) {
        supplierMapper.update(supplier);
    }
    
    @Override
    public void deleteSupplier(Long id) {
        supplierMapper.delete(id);
    }
}