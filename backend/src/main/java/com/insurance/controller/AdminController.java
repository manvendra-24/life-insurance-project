package com.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.insurance.interfaces.IAdminService;
import com.insurance.interfaces.IAuthService;
import com.insurance.request.AdminRegisterRequest;
import com.insurance.request.InsuranceSettingRequest;
import com.insurance.request.TaxSettingRequest;
import com.insurance.response.AdminResponse;
import com.insurance.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/lifeInsurance")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
    IAdminService service;

    @Autowired
    IAuthService authService;
    
    // Update admin
    @PutMapping("/admins/{admin_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update admin -- BY ADMIN")
    public ResponseEntity<String> updateAdmin(@PathVariable String admin_id, @RequestBody AdminRegisterRequest adminRequest) {
        String response = service.updateAdmin(admin_id, adminRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete admin
    @DeleteMapping("/admins/{admin_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete admin -- BY ADMIN")
    public ResponseEntity<String> deleteAdmin(@PathVariable String admin_id) {
        String response = service.deleteAdmin(admin_id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Activate admin
    @PutMapping("/admins/{admin_id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate admin -- BY ADMIN")
    public ResponseEntity<String> activateAdmin(@PathVariable String admin_id) {
        String response = service.activateAdmin(admin_id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get all admins
    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all admins -- BY ADMIN")
    public ResponseEntity<PagedResponse<AdminResponse>> getAllAdmins(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "adminId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) {
        PagedResponse<AdminResponse> adminResponses = service.getAllAdmins(page, size, sortBy, direction);
        return new ResponseEntity<>(adminResponses, HttpStatus.OK);
    }
    
    

    // Create tax setting
    @PostMapping("/tax-setting")
    @Operation(summary= "create Tax settting -- BY admin")
    public ResponseEntity<String> createTaxSetting(@RequestBody TaxSettingRequest taxSettingRequest) {
        return new ResponseEntity<>(service.createTaxSetting(taxSettingRequest), HttpStatus.CREATED);
    }
    
    //create insurance setting
    @PostMapping("/insurance-setting")
    @Operation(summary= "create insurance setting -- BY admin")
    public ResponseEntity<String> createInsuranceSetting(@RequestBody InsuranceSettingRequest insuranceSettingRequestDto){
      return new ResponseEntity<String>(service.createInsuranceSetting(insuranceSettingRequestDto),HttpStatus.CREATED);
      
    }

    
}
