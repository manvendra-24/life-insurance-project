package com.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
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


import com.insurance.interfaces.IStateService;
import com.insurance.request.StateRequest;
import com.insurance.response.StateResponse;
import com.insurance.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/SecureLife.com")
@PreAuthorize("hasRole('ADMIN')")
public class StateController {

  @Autowired
  IStateService service;
  
  	//create state
    @PostMapping("/states")
    @Operation(summary = "Register State -- BY ADMIN")
    public ResponseEntity<String> createState(@RequestBody StateRequest stateRequest){
      String response=service.createState(stateRequest);
      return new ResponseEntity<>(response,HttpStatus.CREATED);
      
    }
    
    @PutMapping("/state/{id}/update")
    @Operation(summary = "Update state -- BY ADMIN")
    public ResponseEntity<String>updateState(@PathVariable("id")String id,@RequestBody  StateRequest stateRequest){
      String response = service.updateState(id,stateRequest);
      return new ResponseEntity<>(response,HttpStatus.OK);
      
    }
    
    //get state by id
    @GetMapping("/state/{id}")
    @Operation(summary = "Get state by id -- BY ADMIN")
    public ResponseEntity<StateResponse> getStateById(@PathVariable("id") String id) {
        StateResponse stateResponse = service.getStateById(id);
        return new ResponseEntity<>(stateResponse, HttpStatus.OK);
    }
  
    //get all states
    @GetMapping("/states")
    @Operation(summary = "Get all states -- BY ADMIN")
    public ResponseEntity<PagedResponse<StateResponse>> getAllStates(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "5") int size,
      @RequestParam(name = "sortBy", defaultValue = "stateId") String sortBy,
      @RequestParam(name = "direction", defaultValue = "asc") String direction)  {
      return new ResponseEntity<PagedResponse<StateResponse>>(
        service.getAllStates(page, size, sortBy, direction), HttpStatus.OK);
  }
    
    //deactivate state
    @DeleteMapping("/state/{id}/delete")
    @Operation(summary = "Deactivate state -- BY ADMIN")
    public ResponseEntity<String>deactivateState(@PathVariable(name="id") String id){
      return new ResponseEntity<String>(service.deactivateStateById(id),HttpStatus.OK);
    }
    
    //activate state
    @PutMapping("/state/{id}/active")
    @Operation(summary = "Activate State -- BY ADMIN")
    public ResponseEntity<String>activateState(@PathVariable(name="id") String id){
      return new ResponseEntity<String>(service.activateStateById(id),HttpStatus.OK);
    }
    
}