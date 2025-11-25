package com.gov.investment.model;

import lombok.Data;
import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BusinessOpportunity {

    private BigDecimal id;

    @NotBlank(message = "商机名称不能为空")
    @Size(max = 200, message = "商机名称长度不能超过200个字符")
    private String name;

    @NotBlank(message = "商机描述不能为空")
    @Size(max = 2000, message = "商机描述长度不能超过2000个字符")
    private String description;

    @NotNull(message = "供应商ID不能为空")
    private BigDecimal supplierId;

    @NotBlank(message = "商机状态不能为空")
    @Size(max = 20, message = "商机状态长度不能超过20个字符")
    private String status;

    @DecimalMin(value = "0.00", message = "预估金额不能小于0")
    private BigDecimal estimatedAmount;

    private LocalDateTime expectedCloseTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remark;
}