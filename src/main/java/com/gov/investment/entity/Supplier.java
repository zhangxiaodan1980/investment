package com.gov.investment.entity;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import com.gov.investment.validation.AllowedStatus;

@Data
public class Supplier {
    
    private Long id;
    
    @NotBlank(message = "Supplier name cannot be blank")
    @Size(max = 100, message = "Supplier name cannot exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Contact person cannot be blank")
    @Size(max = 50, message = "Contact person cannot exceed 50 characters")
    private String contactPerson;
    
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid phone number format")
    private String phone;
    
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;
    
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;
    
    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;
    
    @AllowedStatus(message = "Invalid status value. Allowed values: Active, Inactive")
    private String status;
    
    private String createTime;
    
    private String updateTime;
}