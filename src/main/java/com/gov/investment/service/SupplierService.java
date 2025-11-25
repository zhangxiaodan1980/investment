package com.gov.investment.service;

import com.gov.investment.model.Supplier;

import java.util.List;

public interface SupplierService {

    void save(Supplier supplier);

    void update(Supplier supplier);

    void delete(BigDecimal id);

    Supplier findById(BigDecimal id);

    List<Supplier> findByName(String name);

    List<Supplier> findAll();

    boolean isContactPhoneUnique(String contactPhone, BigDecimal id);

    boolean isCreditCodeUnique(String creditCode, BigDecimal id);

    boolean validateSupplier(Supplier supplier);
}
