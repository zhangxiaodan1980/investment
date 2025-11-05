package com.gov.investment.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Supplier {
    // 主键ID
    private Long id;
    
    // 供应商类型：1-公司，2-个人
    private Integer supplierType;
    
    // 公司名称（公司供应商必填）
    private String companyName;
    
    // 统一社会信用代码/营业执照号（公司供应商必填，唯一）
    private String creditCode;
    
    // 个人姓名（个人供应商必填）
    private String personalName;
    
    // 身份证号码（个人供应商必填，唯一）
    private String idCard;
    
    // 联系人
    private String contactPerson;
    
    // 联系电话
    private String phone;
    
    // 电子邮箱
    private String email;
    
    // 地址
    private String address;
    
    // 银行账户
    private String bankAccount;
    
    // 开户行
    private String bankName;
    
    // 税务登记号
    private String taxNumber;
    
    // 外部验证状态：0-未验证，1-验证通过，2-验证失败
    private Integer verificationStatus;
    
    // 验证失败原因
    private String verificationReason;
    
    // 人工审核状态：0-待审核，1-审核通过，2-审核拒绝
    private Integer auditStatus;
    
    // 审核人
    private String auditor;
    
    // 审核时间
    private LocalDateTime auditTime;
    
    // 审核意见
    private String auditOpinion;
    
    // 状态：0-禁用，1-启用
    private Integer status;
    
    // 创建时间
    private LocalDateTime createTime;
    
    // 更新时间
    private LocalDateTime updateTime;
}