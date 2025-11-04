package com.gov.investment.controller;

import com.gov.investment.model.BusinessOpportunity;
import com.gov.investment.service.BusinessOpportunityService;
import com.gov.investment.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/opportunities")
public class BusinessOpportunityController {
    
    @Autowired
    private BusinessOpportunityService businessOpportunityService;
    
    @Autowired
    private ProjectService projectService;
    
    // 文件上传路径，从配置文件中读取
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @PostMapping
    public ResponseEntity<?> createBusinessOpportunity(
            @RequestParam("file") MultipartFile file,
            @RequestParam("opportunityName") String opportunityName,
            @RequestParam("opportunityCode") String opportunityCode,
            @RequestParam("companyName") String companyName,
            @RequestParam("legalPerson") String legalPerson,
            @RequestParam("companyAddress") String companyAddress,
            @RequestParam("contactPerson") String contactPerson,
            @RequestParam("contactPhone") String contactPhone,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("status") String status,
            @RequestParam("projectId") BigDecimal projectId) {
        
        try {
            // 验证项目是否存在
            if (projectService.getProjectById(projectId) == null) {
                return new ResponseEntity<>("关联项目不存在", HttpStatus.BAD_REQUEST);
            }
            
            // 处理文件上传
            String filePath = null;
            if (!file.isEmpty()) {
                // 生成唯一文件名
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;
                
                // 创建上传目录（如果不存在）
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // 保存文件
                File destFile = new File(uploadPath + File.separator + uniqueFilename);
                file.transferTo(destFile);
                
                // 保存文件路径
                filePath = destFile.getAbsolutePath();
            }
            
            // 创建商机实体
            BusinessOpportunity businessOpportunity = new BusinessOpportunity();
            businessOpportunity.setOpportunityName(opportunityName);
            businessOpportunity.setOpportunityCode(opportunityCode);
            businessOpportunity.setCompanyName(companyName);
            businessOpportunity.setLegalPerson(legalPerson);
            businessOpportunity.setCompanyAddress(companyAddress);
            businessOpportunity.setContactPerson(contactPerson);
            businessOpportunity.setContactPhone(contactPhone);
            businessOpportunity.setAmount(amount);
            businessOpportunity.setStatus(status);
            
            // 设置项目关联
            com.gov.investment.model.Project project = new com.gov.investment.model.Project();
            project.setId(projectId);
            businessOpportunity.setProject(project);
            
            // 调用服务创建商机
            int result = businessOpportunityService.createBusinessOpportunity(businessOpportunity);
            
            if (result > 0) {
                return new ResponseEntity<>("商机创建成功", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("商机创建失败", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("文件上传失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getBusinessOpportunityById(@PathVariable BigDecimal id) {
        BusinessOpportunity businessOpportunity = businessOpportunityService.getBusinessOpportunityById(id);
        if (businessOpportunity != null) {
            return new ResponseEntity<>(businessOpportunity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("商机不存在", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBusinessOpportunity(
            @PathVariable BigDecimal id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("opportunityName") String opportunityName,
            @RequestParam("opportunityCode") String opportunityCode,
            @RequestParam("companyName") String companyName,
            @RequestParam("legalPerson") String legalPerson,
            @RequestParam("companyAddress") String companyAddress,
            @RequestParam("contactPerson") String contactPerson,
            @RequestParam("contactPhone") String contactPhone,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("status") String status,
            @RequestParam("projectId") BigDecimal projectId) {
        
        try {
            // 验证商机是否存在
            if (businessOpportunityService.getBusinessOpportunityById(id) == null) {
                return new ResponseEntity<>("商机不存在", HttpStatus.NOT_FOUND);
            }
            
            // 验证项目是否存在
            if (projectService.getProjectById(projectId) == null) {
                return new ResponseEntity<>("关联项目不存在", HttpStatus.BAD_REQUEST);
            }
            
            // 处理文件上传
            String filePath = null;
            if (!file.isEmpty()) {
                // 生成唯一文件名
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;
                
                // 创建上传目录（如果不存在）
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // 保存文件
                File destFile = new File(uploadPath + File.separator + uniqueFilename);
                file.transferTo(destFile);
                
                // 保存文件路径
                filePath = destFile.getAbsolutePath();
            }
            
            // 创建商机实体
            BusinessOpportunity businessOpportunity = new BusinessOpportunity();
            businessOpportunity.setId(id);
            businessOpportunity.setOpportunityName(opportunityName);
            businessOpportunity.setOpportunityCode(opportunityCode);
            businessOpportunity.setCompanyName(companyName);
            businessOpportunity.setLegalPerson(legalPerson);
            businessOpportunity.setCompanyAddress(companyAddress);
            businessOpportunity.setContactPerson(contactPerson);
            businessOpportunity.setContactPhone(contactPhone);
            businessOpportunity.setAmount(amount);
            businessOpportunity.setStatus(status);
            
            // 设置项目关联
            com.gov.investment.model.Project project = new com.gov.investment.model.Project();
            project.setId(projectId);
            businessOpportunity.setProject(project);
            
            // 调用服务更新商机
            int result = businessOpportunityService.updateBusinessOpportunity(businessOpportunity);
            
            if (result > 0) {
                return new ResponseEntity<>("商机更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("商机更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("文件上传失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBusinessOpportunity(@PathVariable BigDecimal id) {
        int result = businessOpportunityService.deleteBusinessOpportunity(id);
        if (result > 0) {
            return new ResponseEntity<>("商机删除成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("商机删除失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<BusinessOpportunity>> getAllBusinessOpportunities() {
        List<BusinessOpportunity> businessOpportunities = businessOpportunityService.getAllBusinessOpportunities();
        return new ResponseEntity<>(businessOpportunities, HttpStatus.OK);
    }
    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<BusinessOpportunity>> getBusinessOpportunitiesByProjectId(@PathVariable BigDecimal projectId) {
        List<BusinessOpportunity> businessOpportunities = businessOpportunityService.getBusinessOpportunitiesByProjectId(projectId);
        return new ResponseEntity<>(businessOpportunities, HttpStatus.OK);
    }
}
