package com.gov.investment.entity;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import com.gov.investment.validation.AllowedStage;
import java.math.BigDecimal;

@Data
public class BusinessOpportunity {
    
    private Long id;
    
    @NotBlank(message = "Opportunity name cannot be blank")
    @Size(max = 100, message = "Opportunity name cannot exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Opportunity description cannot be blank")
    @Size(max = 500, message = "Opportunity description cannot exceed 500 characters")
    private String description;
    
    @NotNull(message = "Expected revenue cannot be null")
    @DecimalMin(value = "0.01", message = "Expected revenue must be greater than 0")
    private BigDecimal expectedRevenue;
    
    @NotBlank(message = "Stage cannot be blank")
    @Size(max = 50, message = "Stage cannot exceed 50 characters")
    @AllowedStage(message = "Invalid stage value. Allowed values: Prospect, Qualified, Proposal, Negotiation, Closed Won, Closed Lost")
    private String stage;
    
    @NotBlank(message = "Customer name cannot be blank")
    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    private String customerName;
    
    @NotBlank(message = "Customer contact cannot be blank")
    @Size(max = 50, message = "Customer contact cannot exceed 50 characters")
    private String customerContact;
    
    @Size(max = 200, message = "Customer address cannot exceed 200 characters")
    private String customerAddress;
    
    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;
    
    @AllowedStatus(message = "Invalid status value. Allowed values: Active, Inactive")
    private String status;
    
    private String createTime;
    
    private String updateTime;
}