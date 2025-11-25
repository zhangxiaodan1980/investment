package com.gov.investment.service.impl;

import com.gov.investment.mapper.SupplierMapper;
import com.gov.investment.model.Supplier;
import com.gov.investment.service.SupplierService;
import com.gov.investment.util.OkHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Value("${external.api.credit-code-validation.url}")
    private String creditCodeValidationUrl;

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public void save(Supplier supplier) {
        supplier.setCreateTime(LocalDateTime.now());
        supplier.setUpdateTime(LocalDateTime.now());
        supplier.setStatus("ACTIVE");
        supplierMapper.insert(supplier);
    }

    @Override
    public void update(Supplier supplier) {
        supplier.setUpdateTime(LocalDateTime.now());
        supplierMapper.update(supplier);
    }

    @Override
    public void delete(BigDecimal id) {
        supplierMapper.delete(id);
    }

    @Override
    public Supplier findById(BigDecimal id) {
        return supplierMapper.findById(id);
    }

    @Override
    public List<Supplier> findByName(String name) {
        return supplierMapper.findByName(name);
    }

    @Override
    public List<Supplier> findAll() {
        return supplierMapper.findAll();
    }

    @Override
    public boolean isContactPhoneUnique(String contactPhone, BigDecimal id) {
        int count = supplierMapper.countByContactPhone(contactPhone);
        if (id == null) {
            return count == 0;
        } else {
            Supplier existingSupplier = supplierMapper.findById(id);
            return existingSupplier != null && existingSupplier.getContactPhone().equals(contactPhone) ? count == 1 : count == 0;
        }
    }

    @Override
    public boolean isCreditCodeUnique(String creditCode, BigDecimal id) {
        if (creditCode == null || creditCode.isEmpty()) {
            return true;
        }
        int count = supplierMapper.countByCreditCode(creditCode);
        if (id == null) {
            return count == 0;
        } else {
            Supplier existingSupplier = supplierMapper.findById(id);
            return existingSupplier != null && existingSupplier.getCreditCode().equals(creditCode) ? count == 1 : count == 0;
        }
    }

    @Override
    public boolean validateSupplier(Supplier supplier) {
        // 基本校验已经通过JSR-380注解完成
        // 这里可以添加更多的业务逻辑校验
        if (!isContactPhoneUnique(supplier.getContactPhone(), supplier.getId())) {
            return false;
        }
        if (!isCreditCodeUnique(supplier.getCreditCode(), supplier.getId())) {
            return false;
        }
        // 外部API校验统一社会信用代码
        if (supplier.getCreditCode() != null && !supplier.getCreditCode().isEmpty()) {
            try {
                boolean isValid = validateCreditCodeWithExternalApi(supplier.getCreditCode());
                if (!isValid) {
                    logger.error("统一社会信用代码校验失败: {}", supplier.getCreditCode());
                    return false;
                }
            } catch (IOException e) {
                logger.error("调用外部API校验统一社会信用代码失败: {}", e.getMessage());
                // 可以选择在这里返回false或者继续，根据业务需求
                // return false;
            }
        }
        return true;
    }

    /**
     * 调用外部API校验统一社会信用代码
     * @param creditCode 统一社会信用代码
     * @return 是否有效
     * @throws IOException IO异常
     */
    private boolean validateCreditCodeWithExternalApi(String creditCode) throws IOException {
        // 这里只是示例，实际应该替换为真实的外部API地址
        String url = creditCodeValidationUrl + "?code=" + creditCode;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        // 可以添加API密钥等认证信息
        // headers.put("Authorization", "Bearer your-api-key");
        String response = OkHttpUtils.get(url, headers);
        // 解析响应结果，这里假设响应是JSON格式，包含success字段
        // 实际应该根据真实的API响应格式进行解析
        return response != null && response.contains("success");
    }
}
