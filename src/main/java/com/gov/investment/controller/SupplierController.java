package com.gov.investment.controller;

import com.gov.investment.model.Supplier;
import com.gov.investment.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/supplier")
@Slf4j
@Validated
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/add")
    public ResponseEntity<?> addSupplier(@Validated @RequestBody Supplier supplier) {
        try {
            boolean success = supplierService.addSupplier(supplier);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "供应商添加成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "供应商添加失败"));
            }
        } catch (Exception e) {
            log.error("添加供应商失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSupplier(@Validated @RequestBody Supplier supplier) {
        try {
            boolean success = supplierService.updateSupplier(supplier);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "供应商更新成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "供应商更新失败"));
            }
        } catch (Exception e) {
            log.error("更新供应商失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        try {
            boolean success = supplierService.deleteSupplier(id);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "供应商删除成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "供应商删除失败"));
            }
        } catch (Exception e) {
            log.error("删除供应商失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        try {
            Supplier supplier = supplierService.getSupplierById(id);
            if (supplier != null) {
                return ResponseEntity.ok(Map.of("success", true, "data", supplier));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "供应商不存在"));
            }
        } catch (Exception e) {
            log.error("获取供应商失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllSuppliers() {
        try {
            List<Supplier> suppliers = supplierService.getAllSuppliers();
            return ResponseEntity.ok(Map.of("success", true, "data", suppliers));
        } catch (Exception e) {
            log.error("获取供应商列表失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSuppliers(@RequestParam(required = false) String name, 
                                             @RequestParam(required = false) String type, 
                                             @RequestParam(required = false) String status) {
        try {
            List<Supplier> suppliers = supplierService.getSuppliersByCondition(name, type, status);
            return ResponseEntity.ok(Map.of("success", true, "data", suppliers));
        } catch (Exception e) {
            log.error("搜索供应商失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }
}