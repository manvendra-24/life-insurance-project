package com.insurance.controller;


import com.insurance.request.OtpForgetPasswordRequest;
import com.insurance.security.JwtTokenProvider;
import com.insurance.service.EmailService;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ForgetPasswordController {

	@Autowired
    private EmailService emailService;

    
    @PostMapping("/request-otp")
    public ResponseEntity<String> requestOtp(@RequestBody OtpForgetPasswordRequest otpForgetPasswordRequest) {
        String response = emailService.sendOtpForForgetPassword(otpForgetPasswordRequest.getUsernameOrEmail());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

  
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpForgetPasswordRequest otpForgetPasswordRequest) {
        String response = emailService.verifyOtp(otpForgetPasswordRequest.getOtp());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   
    @PutMapping("/reset-password")
    public ResponseEntity<String> setNewPassword(@RequestBody OtpForgetPasswordRequest otpForgetPasswordRequest) {
        String response = emailService.setNewPassword(otpForgetPasswordRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


