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


import com.insurance.interfaces.ICityService;
import com.insurance.request.CityRequest;
import com.insurance.response.CityResponse;
import com.insurance.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/SecureLife.com")
public class CityController {

  @Autowired
  ICityService service;
  
  
  	//create city
    @PostMapping("/cities")
    @Operation(summary = "Register City -- BY ADMIN")
    public ResponseEntity<String> createCity(@RequestBody CityRequest cityRequest){
      String response = service.createCity(cityRequest);
      return new ResponseEntity<>(response,HttpStatus.CREATED);
      
    }
    
    //update city
    @PutMapping("/city/{id}")
    @Operation(summary = "Update City -- BY ADMIN")
    public ResponseEntity<String>updateCity(@PathVariable(name="id") String id,@RequestBody CityRequest cityRequest){
      return new ResponseEntity<String>(service.updateCity(id,cityRequest),HttpStatus.OK);
    }
    
    //deactivate city
    @DeleteMapping("/city/{id}")
    @Operation(summary = "Deactivate city -- BY ADMIN")
    public ResponseEntity<String>deactivateCity(@PathVariable(name="id") String id){
      return new ResponseEntity<String>(service.deactivateCity(id),HttpStatus.OK);
      
    }
    
    //get city by id
    @GetMapping("/city/{id}")
    @Operation(summary = "Get city by Id -- BY ADMIN")
    public ResponseEntity<CityResponse>getCityById(@PathVariable(name="id") String id){
      return new ResponseEntity<CityResponse>(service.getCityById(id),HttpStatus.OK);
    }
    
    //activate city
    @PutMapping("/city/{id}/activate")
    @Operation(summary = "Activate City -- BY ADMIN")
    public ResponseEntity<String>activateCity(@PathVariable(name="id") String id){
      return new ResponseEntity<String>(service.activateCity(id),HttpStatus.OK);
    }
    
    //get all city
    @GetMapping("/cities")
    @Operation(summary = "Get all Cities -- BY ADMIN")
    public ResponseEntity<PagedResponse<CityResponse>> getAllCities(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "5") int size,
      @RequestParam(name = "sortBy", defaultValue = "cityId") String sortBy,
      @RequestParam(name = "direction", defaultValue = "asc") String direction){
      return new ResponseEntity<PagedResponse<CityResponse>>(service.getAllCities(page, size, sortBy, direction),HttpStatus.OK);
      
      
    }
}