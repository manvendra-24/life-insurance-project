package com.insurance.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.exceptions.ApiException;
import com.insurance.interfaces.IWithdrawalService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lifeInsurance")
public class WithdrawalController {

	
	
	@Autowired
	private IWithdrawalService service;
	
	
	//withdrawal request
	@PostMapping("/policy/{policy_id}/withdrawal")
	 @Operation(summary= "request withdrawal -- BY customer")
	public ResponseEntity<String> withdrawalRequest(HttpServletRequest request, @PathVariable("policy_id") String policy_id) {
	    	String authorizationHeader = request.getHeader("Authorization");
	      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	          String token = authorizationHeader.substring(7);
	          service.withdrawalRequest(token, policy_id);
	          return new ResponseEntity<>("Withdrawal request sent", HttpStatus.CREATED);
	      }
	      throw new ApiException("User is unauthorized");
	  }
	
	
	// Approve withdrawal
    @PostMapping("/withdrawals/{withdrawal_id}/approve")
    @Operation(summary= "approve withdrawal -- BY admin")
    public ResponseEntity<String> approveWithdrawal(HttpServletRequest request,@PathVariable long withdrawal_id) {
    	String authorizationHeader = request.getHeader("Authorization");
	      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	          String token = authorizationHeader.substring(7);
	          String response = service.approveWithdrawal(token, withdrawal_id);
	          return new ResponseEntity<>(response, HttpStatus.OK);
	      }
	      throw new ApiException("User is unauthorized");
    }
    
    // Reject withdrawal
    @PostMapping("/withdrawals/{withdrawal_id}/reject")
    @Operation(summary= "reject withdrawal -- BY admin")
    public ResponseEntity<String> rejectWithdrawal(HttpServletRequest request,@PathVariable long withdrawal_id) {
    	String authorizationHeader = request.getHeader("Authorization");
	      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	          String token = authorizationHeader.substring(7);
	          String response = service.rejectWithdrawal(token, withdrawal_id);
	          return new ResponseEntity<>(response, HttpStatus.OK);
	      }
	      throw new ApiException("User is unauthorized");
    }
	
}
