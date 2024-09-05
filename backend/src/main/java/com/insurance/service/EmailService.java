package com.insurance.service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insurance.entities.OtpEntity;
import com.insurance.entities.User;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.repository.OtpRepository;
import com.insurance.repository.UserRepository;
import com.insurance.request.OtpForgetPasswordRequest;

import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

  
    public void sendEmail(String toMail, String subject, String emailBody) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromMail);
        mailMessage.setTo(toMail);
        mailMessage.setSubject(subject);
        mailMessage.setText(emailBody);
        javaMailSender.send(mailMessage);
    }

    
    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

      public void sendOtpEmail(String toMail, String otp) {
        String subject = "Password Reset OTP";
        String emailBody = "Your OTP for resetting the password is: " + otp;
        sendEmail(toMail, subject, emailBody);
    }

   
    public String sendOtpForForgetPassword(String username) {
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            throw new ResourceNotFoundException("User not available for username: " + username);
        }
        User user = oUser.get();

        String otp = generateOTP();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
        OtpEntity otpEntity = new OtpEntity(user.getUsername(), otp, expirationTime);
        otpRepository.save(otpEntity);

        sendOtpEmail(user.getEmail(), otp);

        return "OTP sent to your registered email.";
    }

   
    public String verifyOtp(String otp) {
        Optional<OtpEntity> otpEntityOptional = otpRepository.findByOtp(otp);
        if (otpEntityOptional.isEmpty() || otpEntityOptional.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new ApiException("Invalid or expired OTP");
        }

        return "OTP verified successfully";
    }

   
    @Transactional
    public String setNewPassword(OtpForgetPasswordRequest forgetPasswordRequest) {
        String username = forgetPasswordRequest.getUsernameOrEmail();
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            throw new ResourceNotFoundException("User not available for username: " + username);
        }
        User user = oUser.get();

       
        if (!forgetPasswordRequest.getNewPassword().equals(forgetPasswordRequest.getConfirmPassword())) {
            throw new ApiException("Confirm password does not match new password");
        }

       
        user.setPassword(passwordEncoder.encode(forgetPasswordRequest.getNewPassword()));
        userRepository.save(user);

       
        otpRepository.deleteByUsername(user.getUsername());

        return "Password updated successfully";
    }
}