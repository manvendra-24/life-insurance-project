package com.insurance.controller;

import org.slf4j.LoggerFactory;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.response.JWTAuthResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

import com.insurance.request.LoginDto;
import com.insurance.request.ProfileRequest;
import com.insurance.request.AdminRegisterRequest;
import com.insurance.request.ChangePasswordRequest;
import com.insurance.interfaces.IAuthService;


@RestController
@RequestMapping("/SecureLife.com")
public class AuthController {

    private IAuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API
    @PostMapping("/login")
    @Operation(summary = "Login  -- For All")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
      logger.info("A user is trying to login: " + loginDto.getUsernameOrEmail());
        String token = authService.login(loginDto);
        String role = authService.getRole(token);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setUsername(loginDto.getUsernameOrEmail());
        jwtAuthResponse.setRole(role);
        

        return ResponseEntity.ok()
              .header("Authorization", token)
              .body(jwtAuthResponse);    
        }
    
    

    // Build Register REST API
    @PostMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register Admin -- BY ADMIN")
    public ResponseEntity<String> register(@RequestBody AdminRegisterRequest registerDto){
      logger.info("A user is trying to register ");
        String response = authService.registerAdmin(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    
    @PutMapping("/profile/update")
    @Operation(summary = "Profile update  -- For All")
    public ResponseEntity<String>updateProfile(HttpServletRequest request,@RequestBody ProfileRequest profileRequest) throws AccessDeniedException{
    	 String authorizationHeader = request.getHeader("Authorization");
         if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
             String token = authorizationHeader.substring(7);
           String response = authService.profileUpdate(token,profileRequest );
           return new ResponseEntity<>(response, HttpStatus.CREATED);
         }
         throw new AccessDeniedException("User is unauthorized");
         

    }
    
    @PutMapping("/password/change")
    @Operation(summary = "Password change  -- For All")
    public ResponseEntity<String>changePassword(HttpServletRequest request,@RequestBody ChangePasswordRequest profileRequest) throws AccessDeniedException{
    	 String authorizationHeader = request.getHeader("Authorization");
         if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
             String token = authorizationHeader.substring(7);
           String response = authService.changePassword(token,profileRequest );
           return new ResponseEntity<>(response, HttpStatus.CREATED);
         }
         throw new AccessDeniedException("User is unauthorized");
    }
   
}