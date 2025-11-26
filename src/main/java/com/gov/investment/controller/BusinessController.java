package com.gov.investment.controller;

import com.gov.investment.entity.BusinessOpportunity;
import com.gov.investment.entity.Supplier;
import com.gov.investment.model.LoginRequest;
import com.gov.investment.model.UserInfo;
import com.gov.investment.service.BusinessOpportunityService;
import com.gov.investment.service.SupplierService;
import com.gov.investment.service.UserService;
import com.gov.investment.util.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/busi")
@Slf4j
public class BusinessController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private BusinessOpportunityService businessOpportunityService;

    @PostMapping("/info")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getInfo(){
        return ResponseEntity.ok("OK");
    }

    // Supplier CRUD operations
    @GetMapping("/supplier")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllSuppliers(){
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/supplier/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id){
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(supplier);
    }

    @PostMapping("/supplier")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSupplier(@Valid @RequestBody Supplier supplier){
        supplierService.createSupplier(supplier);
        return ResponseEntity.ok("Supplier created successfully");
    }

    @PutMapping("/supplier")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSupplier(@Valid @RequestBody Supplier supplier){
        supplierService.updateSupplier(supplier);
        return ResponseEntity.ok("Supplier updated successfully");
    }

    @DeleteMapping("/supplier/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id){
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok("Supplier deleted successfully");
    }

    // Business Opportunity CRUD operations
    @GetMapping("/opportunity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBusinessOpportunities(){
        return ResponseEntity.ok(businessOpportunityService.getAllBusinessOpportunities());
    }

    @GetMapping("/opportunity/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBusinessOpportunityById(@PathVariable Long id){
        BusinessOpportunity opportunity = businessOpportunityService.getBusinessOpportunityById(id);
        if (opportunity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opportunity);
    }

    @PostMapping("/opportunity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBusinessOpportunity(@Valid @RequestBody BusinessOpportunity opportunity){
        businessOpportunityService.createBusinessOpportunity(opportunity);
        return ResponseEntity.ok("Business opportunity created successfully");
    }

    @PutMapping("/opportunity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBusinessOpportunity(@Valid @RequestBody BusinessOpportunity opportunity){
        businessOpportunityService.updateBusinessOpportunity(opportunity);
        return ResponseEntity.ok("Business opportunity updated successfully");
    }

    @DeleteMapping("/opportunity/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBusinessOpportunity(@PathVariable Long id){
        businessOpportunityService.deleteBusinessOpportunity(id);
        return ResponseEntity.ok("Business opportunity deleted successfully");
    }
}
