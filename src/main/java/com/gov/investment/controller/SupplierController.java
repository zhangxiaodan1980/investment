package com.gov.investment.controller;

import com.gov.investment.model.Supplier;
import com.gov.investment.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody Supplier supplier, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        if (!supplierService.validateSupplier(supplier)) {
            return ResponseEntity.badRequest().body("供应商信息校验失败，可能是联系电话或统一社会信用代码重复");
        }

        try {
            supplierService.save(supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(supplier);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("创建供应商失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable BigDecimal id, @Valid @RequestBody Supplier supplier, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        supplier.setId(id);
        if (!supplierService.validateSupplier(supplier)) {
            return ResponseEntity.badRequest().body("供应商信息校验失败，可能是联系电话或统一社会信用代码重复");
        }

        try {
            supplierService.update(supplier);
            return ResponseEntity.ok(supplier);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新供应商失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable BigDecimal id) {
        try {
            supplierService.delete(id);
            return ResponseEntity.ok("供应商删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除供应商失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable BigDecimal id) {
        try {
            Supplier supplier = supplierService.findById(id);
            if (supplier == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(supplier);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取供应商失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSuppliers(@RequestParam String name) {
        try {
            List<Supplier> suppliers = supplierService.findByName(name);
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("搜索供应商失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSuppliers() {
        try {
            List<Supplier> suppliers = supplierService.findAll();
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取所有供应商失败: " + e.getMessage());
        }
    }
}
