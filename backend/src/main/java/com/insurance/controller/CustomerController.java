package com.insurance.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.entities.Agent;
import com.insurance.interfaces.ICustomerService;
import com.insurance.request.CustomerRegisterRequest;
import com.insurance.response.CustomerResponse;
import com.insurance.util.PagedResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/lifeInsurance")
public class CustomerController {

  
  ICustomerService service;

  public CustomerController(ICustomerService service) {
	super();
	this.service =service;
}


  @PostMapping("/customer")
  @PreAuthorize("hasRole('AGENT')")
  public ResponseEntity<String>registerCustomer(HttpServletRequest request, @Valid @RequestBody CustomerRegisterRequest registerDto) throws AccessDeniedException {
	  String authorizationHeader = request.getHeader("Authorization");
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
          String token = authorizationHeader.substring(7);
      String response = service.registerCustomer(token, registerDto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
      }
      throw new AccessDeniedException("User is unauthorized");
  }
  
  @GetMapping("/customers")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<PagedResponse<CustomerResponse>> getAllCustomers(
          @RequestParam (name="page", defaultValue="0") int page,
          @RequestParam (name="size", defaultValue="5") int size,
          @RequestParam (name = "sortBy", defaultValue="customerId") String sortBy,
          @RequestParam (name = "direction", defaultValue="asc") String direction
          ){
        
    PagedResponse<CustomerResponse> customersResponse = service.getAllCustomers(page, size, sortBy, direction);
    return new ResponseEntity<>(customersResponse, HttpStatus.OK);
  }
  
  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<String> updateCustomer(@PathVariable("id") String id, @Valid @RequestBody CustomerRegisterRequest registerDto) {
      String response = service.updateCustomer(id, registerDto);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }
  @DeleteMapping("/deactivate-customer/{id}")
  public ResponseEntity<String> deactivateCustomer(@PathVariable("id") String id) {
      String response = service.deactivateCustomer(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @PutMapping("/activate-customer/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<String> activateCustomer(@PathVariable("id") String id) {
      String response = service.activateCustomer(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }
  @PutMapping("/verify-customer/approve/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<String>verifyCustomer(HttpServletRequest request,@PathVariable("id")String id) throws AccessDeniedException{
  	  String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String response = service.verifyCustomerapprove(token,id);
	       return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
      	  throw new AccessDeniedException("User is unauthorized");
        }
  }
  
  @PutMapping("/verify-customer/reject/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<String>verifyCustomerReject(HttpServletRequest request,@PathVariable("id")String id) throws AccessDeniedException{
  	  String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String response = service.verifyCustomerReject(token,id);
	       return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
      	  throw new AccessDeniedException("User is unauthorized");
        }
  }
  
  

  
}