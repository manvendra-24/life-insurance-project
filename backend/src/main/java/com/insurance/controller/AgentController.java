package com.insurance.controller;

import java.nio.file.AccessDeniedException;

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

import com.insurance.interfaces.IAgentService;
import com.insurance.request.AgentRegisterRequest;
import com.insurance.response.AgentResponse;
import com.insurance.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/SecureLife.com")
public class AgentController {

  @Autowired
  IAgentService service;

  	//add agent
    @PostMapping("/agents")
    @Operation(summary = "Register agent -- BY ADMIN & EMPLOYEE")
    public ResponseEntity<String> register(HttpServletRequest request,@RequestBody AgentRegisterRequest registerDto) throws AccessDeniedException{
      String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
          String response = service.registerAgent(token,registerDto );
          return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        throw new AccessDeniedException("User is unauthorized");
        
    }

    @PutMapping("/agent/{agent_id}")
    @Operation(summary = "Update agent -- BY ADMIN & EMPLOYEE")    
    public ResponseEntity<String> updateAgent(@PathVariable String agent_id, @RequestBody AgentRegisterRequest agentRequest) {
        String response = service.updateAgent(agent_id, agentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // delete agent
    @DeleteMapping("/agent/{agent_id}")
    @Operation(summary = "Delete agent -- BY ADMIN & EMPLOYEE")
    public ResponseEntity<String> deleteAgent(@PathVariable String agent_id) {
        String response = service.deleteAgent(agent_id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // activate agent
    @PutMapping("/agent/{agent_id}/active")
    @Operation(summary = "Activate agent -- BY ADMIN & EMPLOYEE")    
    public ResponseEntity<String> activateAgent(@PathVariable String agent_id) {
        String response = service.activateAgent(agent_id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //get all agents
    @GetMapping("/agents")
    @Operation(summary = "Get all agents -- BY ADMIN & EMPLOYEE")    
    public ResponseEntity<PagedResponse<AgentResponse>> getAllAgents(
            @RequestParam (name="page", defaultValue="0") int page,
            @RequestParam (name="size", defaultValue="5") int size,
            @RequestParam (name = "sortBy", defaultValue="agentId") String sortBy,
            @RequestParam (name = "direction", defaultValue="asc") String direction
            ){
          
      PagedResponse<AgentResponse> agentResponses = service.getAllAgents(page, size, sortBy, direction);
      return new ResponseEntity<>(agentResponses, HttpStatus.OK);
    }
  

}