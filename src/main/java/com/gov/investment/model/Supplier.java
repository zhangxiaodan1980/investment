package com.gov.investment.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
public class Supplier {
    private Long id;

    @NotBlank(message = "供应商名称不能为空")
    @Size(max = 100, message = "供应商名称长度不能超过100个字符")
    private String name;

    @NotBlank(message = "供应商类型不能为空")
    @Size(max = 50, message = "供应商类型长度不能超过50个字符")
    private String type;

    @NotBlank(message = "联系人不能为空")
    @Size(max = 50, message = "联系人长度不能超过50个字符")
    private String contactPerson;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    @Size(max = 200, message = "联系邮箱长度不能超过200个字符")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "联系邮箱格式不正确")
    private String contactEmail;

    @Size(max = 500, message = "地址长度不能超过500个字符")
    private String address;

    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remark;

    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}