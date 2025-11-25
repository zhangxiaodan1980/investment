package com.gov.investment.service.impl;

import com.gov.investment.mapper.BusinessOpportunityMapper;
import com.gov.investment.mapper.SupplierMapper;
import com.gov.investment.model.BusinessOpportunity;
import com.gov.investment.model.Supplier;
import com.gov.investment.service.BusinessOpportunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BusinessOpportunityServiceImpl implements BusinessOpportunityService {

    @Autowired
    private BusinessOpportunityMapper businessOpportunityMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public boolean addBusinessOpportunity(BusinessOpportunity businessOpportunity) {
        if (!validateBusinessOpportunity(businessOpportunity)) {
            log.error("商机信息校验失败: {}", businessOpportunity);
            return false;
        }
        businessOpportunity.setCreateTime(LocalDateTime.now());
        businessOpportunity.setUpdateTime(LocalDateTime.now());
        return businessOpportunityMapper.insert(businessOpportunity) > 0;
    }

    @Override
    public boolean updateBusinessOpportunity(BusinessOpportunity businessOpportunity) {
        if (!validateBusinessOpportunity(businessOpportunity)) {
            log.error("商机信息校验失败: {}", businessOpportunity);
            return false;
        }
        businessOpportunity.setUpdateTime(LocalDateTime.now());
        return businessOpportunityMapper.update(businessOpportunity) > 0;
    }

    @Override
    public boolean deleteBusinessOpportunity(Long id) {
        if (id == null || id <= 0) {
            log.error("商机ID无效: {}", id);
            return false;
        }
        return businessOpportunityMapper.deleteById(id) > 0;
    }

    @Override
    public BusinessOpportunity getBusinessOpportunityById(Long id) {
        if (id == null || id <= 0) {
            log.error("商机ID无效: {}", id);
            return null;
        }
        return businessOpportunityMapper.selectById(id);
    }

    @Override
    public List<BusinessOpportunity> getAllBusinessOpportunities() {
        return businessOpportunityMapper.selectAll();
    }

    @Override
    public List<BusinessOpportunity> getBusinessOpportunitiesByCondition(String name, String type, String status, Long supplierId) {
        return businessOpportunityMapper.selectByCondition(name, type, status, supplierId);
    }

    @Override
    public boolean validateBusinessOpportunity(BusinessOpportunity businessOpportunity) {
        if (businessOpportunity == null) {
            log.error("商机信息不能为空");
            return false;
        }
        // 基本校验已经通过JSR-380注解完成，这里可以添加额外的业务校验
        // 例如：检查供应商是否存在且状态为ACTIVE
        Supplier supplier = supplierMapper.selectById(businessOpportunity.getSupplierId());
        if (supplier == null || !supplierStatusActive.equals(supplier.getStatus())) {
            log.error("供应商不存在或状态无效: {}", businessOpportunity.getSupplierId());
            return false;
        }
        // 例如：检查商机状态是否合法
        List<String> validStatuses = List.of(businessOpportunityStatusPending, businessOpportunityStatusInProgress, businessOpportunityStatusWon, businessOpportunityStatusLost);
        if (!validStatuses.contains(businessOpportunity.getStatus())) {
            log.error("商机状态无效: {}", businessOpportunity.getStatus());
            return false;
        }
        return true;
    }
}