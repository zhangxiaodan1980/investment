package com.gov.investment.service.impl;

import com.gov.investment.mapper.BusinessOpportunityMapper;
import com.gov.investment.mapper.SupplierMapper;
import com.gov.investment.model.BusinessOpportunity;
import com.gov.investment.model.Supplier;
import com.gov.investment.service.BusinessOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessOpportunityServiceImpl implements BusinessOpportunityService {

    @Autowired
    private BusinessOpportunityMapper businessOpportunityMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public void save(BusinessOpportunity businessOpportunity) {
        businessOpportunity.setCreateTime(LocalDateTime.now());
        businessOpportunity.setUpdateTime(LocalDateTime.now());
        businessOpportunity.setStatus("OPEN");
        businessOpportunityMapper.insert(businessOpportunity);
    }

    @Override
    public void update(BusinessOpportunity businessOpportunity) {
        businessOpportunity.setUpdateTime(LocalDateTime.now());
        businessOpportunityMapper.update(businessOpportunity);
    }

    @Override
    public void delete(BigDecimal id) {
        businessOpportunityMapper.delete(id);
    }

    @Override
    public BusinessOpportunity findById(BigDecimal id) {
        return businessOpportunityMapper.findById(id);
    }

    @Override
    public List<BusinessOpportunity> findByName(String name) {
        return businessOpportunityMapper.findByName(name);
    }

    @Override
    public List<BusinessOpportunity> findBySupplierId(BigDecimal supplierId) {
        return businessOpportunityMapper.findBySupplierId(supplierId);
    }

    @Override
    public List<BusinessOpportunity> findAll() {
        return businessOpportunityMapper.findAll();
    }

    @Override
    public boolean validateBusinessOpportunity(BusinessOpportunity businessOpportunity) {
        // 基本校验已经通过JSR-380注解完成
        // 这里可以添加更多的业务逻辑校验
        // 校验供应商是否存在
        Supplier supplier = supplierMapper.findById(businessOpportunity.getSupplierId());
        if (supplier == null) {
            return false;
        }
        // 校验商机状态是否合法
        if (!isValidStatus(businessOpportunity.getStatus())) {
            return false;
        }
        // 可以添加更多校验，比如外部API校验
        return true;
    }

    private boolean isValidStatus(String status) {
        return status != null && ("OPEN".equals(status) || "IN_PROGRESS".equals(status) || "CLOSED_WON".equals(status) || "CLOSED_LOST".equals(status));
    }
}
