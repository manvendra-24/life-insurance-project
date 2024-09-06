package com.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.interfaces.IInsurancePlanService;
import com.insurance.request.InsurancePlanRequest;
import com.insurance.response.InsurancePlanResponse;
import com.insurance.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/SecureLife.com")
public class InsurancePlanController {

	@Autowired
	IInsurancePlanService service;
	
	//get all insurance plans
    @GetMapping("/plans")
    @Operation(summary= "Get all Insurance Plans -- For ADMIN")
    public ResponseEntity<PagedResponse<InsurancePlanResponse>> getAllInsurancePlans(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "insuranceId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        return new ResponseEntity<>(service.getAllInsurancePlans(page, size, sortBy, direction), HttpStatus.OK);
    }

    //create insurance plan
    @PostMapping("/plans")
    @Operation(summary= "Create Insurance Plans -- For ADMIN")
    public ResponseEntity<String> createInsurancePlan(@RequestBody InsurancePlanRequest planRequest) {
        String response = service.createInsurancePlan(planRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //update insurance plan
    @PutMapping("/plan/{id}/update")
    @Operation(summary= "Update Insurance Plans -- For ADMIN")
    public ResponseEntity<String> updateInsurancePlan(@PathVariable String id, @RequestBody InsurancePlanRequest planRequest) {
        String response = service.updateInsurancePlan(id, planRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //activate insurance plan
    @PutMapping("/plan/{id}/activate")
    @Operation(summary= "Activate Insurance Plans -- For ADMIN")
    public ResponseEntity<String> activateInsurancePlan(@PathVariable String id) {
        String response = service.activateInsurancePlan(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //delete insurance plan
    @DeleteMapping("/plan/{id}/delete")
    @Operation(summary= "Delete Insurance Pla -- For ADMIN")
    public ResponseEntity<String> deleteInsurancePlan(@PathVariable String id) {
        String response = service.deleteInsurancePlan(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
