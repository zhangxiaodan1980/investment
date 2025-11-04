package com.gov.investment.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "business_opportunity", uniqueConstraints = {
    @UniqueConstraint(columnNames = "opportunity_name"),
    @UniqueConstraint(columnNames = "opportunity_code")
})
public class BusinessOpportunity {
    
    @Id
    @Column(name = "id", precision = 20, scale = 0)
    private BigDecimal id;
    
    @Column(name = "opportunity_name", nullable = false, length = 100)
    @NotEmpty(message = "商机名称不能为空")
    @Size(max = 100, message = "商机名称不能超过100个字符")
    private String opportunityName;
    
    @Column(name = "opportunity_code", nullable = false, length = 50)
    @NotEmpty(message = "商机编码不能为空")
    @Size(max = 50, message = "商机编码不能超过50个字符")
    private String opportunityCode;
    
    @Column(name = "company_name", nullable = false, length = 100)
    @NotEmpty(message = "公司名称不能为空")
    @Size(max = 100, message = "公司名称不能超过100个字符")
    private String companyName;
    
    @Column(name = "legal_person", nullable = false, length = 50)
    @NotEmpty(message = "法人姓名不能为空")
    @Size(max = 50, message = "法人姓名不能超过50个字符")
    private String legalPerson;
    
    @Column(name = "company_address", length = 200)
    @Size(max = 200, message = "公司地址不能超过200个字符")
    private String companyAddress;
    
    @Column(name = "contact_person", length = 50)
    @Size(max = 50, message = "联系人不能超过50个字符")
    private String contactPerson;
    
    @Column(name = "contact_phone", length = 20)
    @Size(max = 20, message = "联系电话不能超过20个字符")
    private String contactPhone;
    
    @Column(name = "amount", precision = 18, scale = 2)
    @Digits(integer = 16, fraction = 2, message = "金额格式不正确")
    private BigDecimal amount;
    
    @Column(name = "status", nullable = false, length = 20)
    @NotEmpty(message = "状态不能为空")
    @Size(max = 20, message = "状态不能超过20个字符")
    private String status;
    
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    
    // 商机和项目的多对一关联
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
