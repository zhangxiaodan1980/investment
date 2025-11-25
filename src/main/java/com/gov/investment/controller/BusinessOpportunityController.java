package com.gov.investment.controller;

import com.gov.investment.model.BusinessOpportunity;
import com.gov.investment.service.BusinessOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/business-opportunities")
public class BusinessOpportunityController {

    @Autowired
    private BusinessOpportunityService businessOpportunityService;

    @PostMapping
    public ResponseEntity<?> createBusinessOpportunity(@Valid @RequestBody BusinessOpportunity businessOpportunity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        if (!businessOpportunityService.validateBusinessOpportunity(businessOpportunity)) {
            return ResponseEntity.badRequest().body("商机信息校验失败，可能是供应商不存在或状态不合法");
        }

        try {
            businessOpportunityService.save(businessOpportunity);
            return ResponseEntity.status(HttpStatus.CREATED).body(businessOpportunity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("创建商机失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBusinessOpportunity(@PathVariable BigDecimal id, @Valid @RequestBody BusinessOpportunity businessOpportunity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        businessOpportunity.setId(id);
        if (!businessOpportunityService.validateBusinessOpportunity(businessOpportunity)) {
            return ResponseEntity.badRequest().body("商机信息校验失败，可能是供应商不存在或状态不合法");
        }

        try {
            businessOpportunityService.update(businessOpportunity);
            return ResponseEntity.ok(businessOpportunity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新商机失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBusinessOpportunity(@PathVariable BigDecimal id) {
        try {
            businessOpportunityService.delete(id);
            return ResponseEntity.ok("商机删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除商机失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBusinessOpportunityById(@PathVariable BigDecimal id) {
        try {
            BusinessOpportunity businessOpportunity = businessOpportunityService.findById(id);
            if (businessOpportunity == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(businessOpportunity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取商机失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBusinessOpportunities(@RequestParam String name) {
        try {
            List<BusinessOpportunity> businessOpportunities = businessOpportunityService.findByName(name);
            return ResponseEntity.ok(businessOpportunities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("搜索商机失败: " + e.getMessage());
        }
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<?> getBusinessOpportunitiesBySupplierId(@PathVariable BigDecimal supplierId) {
        try {
            List<BusinessOpportunity> businessOpportunities = businessOpportunityService.findBySupplierId(supplierId);
            return ResponseEntity.ok(businessOpportunities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取供应商商机失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBusinessOpportunities() {
        try {
            List<BusinessOpportunity> businessOpportunities = businessOpportunityService.findAll();
            return ResponseEntity.ok(businessOpportunities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取所有商机失败: " + e.getMessage());
        }
    }
}
