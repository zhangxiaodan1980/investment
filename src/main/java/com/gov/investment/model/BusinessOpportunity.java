package com.gov.investment.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Data
public class BusinessOpportunity {
    private Long id;

    @NotBlank(message = "商机名称不能为空")
    @Size(max = 200, message = "商机名称长度不能超过200个字符")
    private String name;

    @NotBlank(message = "商机类型不能为空")
    @Size(max = 50, message = "商机类型长度不能超过50个字符")
    private String type;

    @NotNull(message = "供应商ID不能为空")
    private Long supplierId;

    @NotBlank(message = "商机状态不能为空")
    @Size(max = 50, message = "商机状态长度不能超过50个字符")
    private String status;

    @Min(value = 0, message = "预计金额不能为负数")
    private Double estimatedAmount;

    @Size(max = 1000, message = "商机描述长度不能超过1000个字符")
    private String description;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}