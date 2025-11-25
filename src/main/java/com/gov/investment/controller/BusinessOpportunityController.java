package com.gov.investment.controller;

import com.gov.investment.model.BusinessOpportunity;
import com.gov.investment.service.BusinessOpportunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/business")
@Slf4j
@Validated
public class BusinessOpportunityController {

    @Autowired
    private BusinessOpportunityService businessOpportunityService;

    @PostMapping("/add")
    public ResponseEntity<?> addBusinessOpportunity(@Validated @RequestBody BusinessOpportunity businessOpportunity) {
        try {
            boolean success = businessOpportunityService.addBusinessOpportunity(businessOpportunity);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "商机添加成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "商机添加失败"));
            }
        } catch (Exception e) {
            log.error("添加商机失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBusinessOpportunity(@Validated @RequestBody BusinessOpportunity businessOpportunity) {
        try {
            boolean success = businessOpportunityService.updateBusinessOpportunity(businessOpportunity);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "商机更新成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "商机更新失败"));
            }
        } catch (Exception e) {
            log.error("更新商机失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBusinessOpportunity(@PathVariable Long id) {
        try {
            boolean success = businessOpportunityService.deleteBusinessOpportunity(id);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "商机删除成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "商机删除失败"));
            }
        } catch (Exception e) {
            log.error("删除商机失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getBusinessOpportunityById(@PathVariable Long id) {
        try {
            BusinessOpportunity businessOpportunity = businessOpportunityService.getBusinessOpportunityById(id);
            if (businessOpportunity != null) {
                return ResponseEntity.ok(Map.of("success", true, "data", businessOpportunity));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "商机不存在"));
            }
        } catch (Exception e) {
            log.error("获取商机失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllBusinessOpportunities() {
        try {
            List<BusinessOpportunity> businessOpportunities = businessOpportunityService.getAllBusinessOpportunities();
            return ResponseEntity.ok(Map.of("success", true, "data", businessOpportunities));
        } catch (Exception e) {
            log.error("获取商机列表失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBusinessOpportunities(@RequestParam(required = false) String name, 
                                                       @RequestParam(required = false) String type, 
                                                       @RequestParam(required = false) String status, 
                                                       @RequestParam(required = false) Long supplierId) {
        try {
            List<BusinessOpportunity> businessOpportunities = businessOpportunityService.getBusinessOpportunitiesByCondition(name, type, status, supplierId);
            return ResponseEntity.ok(Map.of("success", true, "data", businessOpportunities));
        } catch (Exception e) {
            log.error("搜索商机失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }
}