package com.gov.investment.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "project", uniqueConstraints = {
    @UniqueConstraint(columnNames = "project_name"),
    @UniqueConstraint(columnNames = "project_code")
})
public class Project {
    
    @Id
    @Column(name = "id", precision = 20, scale = 0)
    private BigDecimal id;
    
    @Column(name = "project_name", nullable = false, length = 100)
    private String projectName;
    
    @Column(name = "project_code", nullable = false, length = 50)
    private String projectCode;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    
    // 项目和商机的一对多关联
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusinessOpportunity> businessOpportunities;
}
