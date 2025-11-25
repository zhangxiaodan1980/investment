package com.gov.investment.service.impl;

import com.gov.investment.mapper.SupplierMapper;
import com.gov.investment.model.Supplier;
import com.gov.investment.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Value("${supplier.status.active}")
    private String supplierStatusActive;

    @Value("${supplier.status.inactive}")
    private String supplierStatusInactive;

    @Override
    public boolean addSupplier(Supplier supplier) {
        if (!validateSupplier(supplier)) {
            log.error("供应商信息校验失败: {}", supplier);
            return false;
        }
        supplier.setCreateTime(LocalDateTime.now());
        supplier.setUpdateTime(LocalDateTime.now());
        supplier.setStatus(supplierStatusActive);
        return supplierMapper.insert(supplier) > 0;
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        if (!validateSupplier(supplier)) {
            log.error("供应商信息校验失败: {}", supplier);
            return false;
        }
        supplier.setUpdateTime(LocalDateTime.now());
        return supplierMapper.update(supplier) > 0;
    }

    @Override
    public boolean deleteSupplier(Long id) {
        if (id == null || id <= 0) {
            log.error("供应商ID无效: {}", id);
            return false;
        }
        // 逻辑删除，更新状态为INACTIVE
        Supplier supplier = supplierMapper.selectById(id);
        if (supplier == null) {
            log.error("供应商不存在: {}", id);
            return false;
        }
        supplier.setStatus(supplierStatusInactive);
        supplier.setUpdateTime(LocalDateTime.now());
        return supplierMapper.update(supplier) > 0;
    }

    @Override
    public Supplier getSupplierById(Long id) {
        if (id == null || id <= 0) {
            log.error("供应商ID无效: {}", id);
            return null;
        }
        return supplierMapper.selectById(id);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierMapper.selectAll();
    }

    @Override
    public List<Supplier> getSuppliersByCondition(String name, String type, String status) {
        return supplierMapper.selectByCondition(name, type, status);
    }

    @Override
    public boolean validateSupplier(Supplier supplier) {
        if (supplier == null) {
            log.error("供应商信息不能为空");
            return false;
        }
        // 基本校验已经通过JSR-380注解完成，这里可以添加额外的业务校验
        // 例如：检查供应商名称是否重复
        // 例如：检查联系电话是否已经被其他供应商使用
        return true;
    }
}