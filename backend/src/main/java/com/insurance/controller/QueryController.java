package com.insurance.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.interfaces.IQueryService;
import com.insurance.request.CustomerQueryRequest;
import com.insurance.request.EmployeeQueryRequest;
import com.insurance.response.CustomerQueryResponse;
import com.insurance.util.PagedResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("lifeInsurance")
public class QueryController {
	
	@Autowired
	IQueryService service;
	
	//createquery
	@PostMapping("/query")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<String>addQuery(HttpServletRequest request,@RequestBody CustomerQueryRequest queryRequest) throws AccessDeniedException{
		 String authorizationHeader = request.getHeader("Authorization");
         if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
             String token = authorizationHeader.substring(7);
             String response = service.addQuery(token,queryRequest);
 	       return new ResponseEntity<>(response,HttpStatus.OK);
         }
         else {
       	  throw new AccessDeniedException("User is unauthorized");
         }
	}
         
         
       //updateresponse
         @PostMapping("/response-query/{id}")
         @PreAuthorize("hasRole('EMPLOYEE')")
         public ResponseEntity<String>addResponseQuery(HttpServletRequest request,@RequestBody EmployeeQueryRequest queryRequest,@PathVariable("id")Long id) throws AccessDeniedException{
    		 String authorizationHeader = request.getHeader("Authorization");
             if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                 String token = authorizationHeader.substring(7);
                 String response = service.addResponseQuery(token,queryRequest,id);
     	       return new ResponseEntity<>(response,HttpStatus.OK);
             }
             else {
           	  throw new AccessDeniedException("User is unauthorized");
             }  
         
	}
	
         
         //getAllQueries
         
         @GetMapping("/queries")
         @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
         public ResponseEntity<PagedResponse<CustomerQueryResponse>> getAllQueries(
                 @RequestParam(name = "page", defaultValue = "0") int page,
                 @RequestParam(name = "size", defaultValue = "5") int size,
                 @RequestParam(name = "sortBy", defaultValue = "queryId") String sortBy,
                 @RequestParam(name = "direction", defaultValue = "asc") String direction) {
             
             PagedResponse<CustomerQueryResponse> queryResponses = service.getAllQueries(page, size, sortBy, direction);
             return new ResponseEntity<>(queryResponses, HttpStatus.OK);
         }
}
