package com.insurance.controller;

import com.insurance.request.PolicyRequest;
import com.insurance.response.CommissionResponse;
import com.insurance.response.PolicyResponse;
import com.insurance.interfaces.IPolicyService;
import com.insurance.exceptions.ApiException;
import com.insurance.util.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/lifeInsurance/policies")
public class PolicyController {

	@Autowired
    private IPolicyService service;

    
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Register policy -- BY CUSTOMER")
    public ResponseEntity<String> createPolicy(HttpServletRequest request, @Valid @RequestBody PolicyRequest policyRequest) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String response = service.createPolicy(token, policyRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        throw new ApiException("User is unauthorized");
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    @Operation(summary = "Get all Policies -- BY EMPLOYEE & ADMIN")
    public ResponseEntity<PagedResponse<PolicyResponse>> getAllPolicies(
    		HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "policyId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
    	String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
        PagedResponse<PolicyResponse> policies = service.getAllPolicies(token, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
        return new ResponseEntity<>(policies, HttpStatus.OK);
        }
        throw new ApiException("User is unauthorized");
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    @Operation(summary = "Get Policies by Customer ID -- BY EMPLOYEE & ADMIN")
    public ResponseEntity<PagedResponse<PolicyResponse>> getPoliciesByCustomerId(
            HttpServletRequest request,
            @PathVariable String customerId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "policyId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            PagedResponse<PolicyResponse> policies = service.getPoliciesByCustomerId(token, customerId, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
            return new ResponseEntity<>(policies, HttpStatus.OK);
        }
        throw new ApiException("User is unauthorized");
    }
    
    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    @Operation(summary = "Get Policies by Agent ID -- BY EMPLOYEE & ADMIN")
    public ResponseEntity<PagedResponse<CommissionResponse>> getCommissionByAgentId(
            HttpServletRequest request,
            @PathVariable String agentId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "policyId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            PagedResponse<CommissionResponse> policies = service.getCommissionByAgentId(token, agentId, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
            return new ResponseEntity<>(policies, HttpStatus.OK);
        }
        throw new ApiException("User is unauthorized");
    }
    
    @GetMapping("/my-commissions")
    @PreAuthorize("hasRole('AGENT')")
    @Operation(summary = "Get My Commission report -- BY Agent")
    public ResponseEntity<PagedResponse<CommissionResponse>> getMyCommission(
            HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "policyId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            PagedResponse<CommissionResponse> policies = service.getMyCommission(token, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
            return new ResponseEntity<>(policies, HttpStatus.OK);
        }
        throw new ApiException("User is unauthorized");
    }

    @GetMapping("/my-policies")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get My Policies -- BY CUSTOMER")
    public ResponseEntity<PagedResponse<PolicyResponse>> getMyPolicies(
            HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "policyId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            PagedResponse<PolicyResponse> policies = service.getMyPolicies(token, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
            return new ResponseEntity<>(policies, HttpStatus.OK);
        }
        throw new ApiException("User is unauthorized");
    }
}
