package com.gov.investment.service;

import com.gov.investment.model.Supplier;

import java.util.List;
import java.util.Map;

public interface SupplierService {
    // 注册供应商
    boolean registerSupplier(Supplier supplier);
    
    // 外部验证供应商信息
    boolean verifySupplier(Long supplierId);
    
    // 人工审核供应商
    boolean auditSupplier(Long supplierId, Integer auditStatus, String auditor, String auditOpinion);
    
    // 查询供应商详情
    Supplier getSupplierById(Long supplierId);
    
    // 分页查询供应商
    Map<String, Object> getSuppliersByPage(int page, int pageSize, Map<String, Object> params);
    
    // 更新供应商信息
    boolean updateSupplier(Supplier supplier);
    
    // 启用/禁用供应商
    boolean updateSupplierStatus(Long supplierId, Integer status);
    
    // 批量同步供应商数据（用于外部系统对接）
    boolean batchSyncSuppliers(List<Supplier> suppliers);
    
    // 检查供应商核心信息唯一性
    boolean checkCoreInfoUnique(Supplier supplier);
}
