package com.gov.investment.mapper;

import com.gov.investment.model.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierMapper {
    // 插入供应商
    void insert(Supplier supplier);
    
    // 根据ID查询供应商
    Supplier selectById(@Param("id") Long id);
    
    // 根据统一社会信用代码查询供应商
    Supplier selectByCreditCode(@Param("creditCode") String creditCode);
    
    // 根据身份证号码查询供应商
    Supplier selectByIdCard(@Param("idCard") String idCard);
    
    // 更新供应商信息
    void update(Supplier supplier);
    
    // 更新供应商验证状态
    void updateVerificationStatus(@Param("id") Long id, @Param("verificationStatus") Integer verificationStatus, @Param("verificationReason") String verificationReason);
    
    // 更新供应商审核状态
    void updateAuditStatus(@Param("id") Long id, @Param("auditStatus") Integer auditStatus, @Param("auditor") String auditor, @Param("auditTime") String auditTime, @Param("auditOpinion") String auditOpinion);
    
    // 分页查询供应商
    List<Supplier> selectByPage(@Param("params") Map<String, Object> params);
    
    // 查询供应商总数
    Long count(@Param("params") Map<String, Object> params);
    
    // 批量插入供应商（用于大数据量同步）
    void batchInsert(@Param("suppliers") List<Supplier> suppliers);
}
