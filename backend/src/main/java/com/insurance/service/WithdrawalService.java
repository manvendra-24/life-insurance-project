package com.insurance.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.entities.Admin;
import com.insurance.entities.Customer;
import com.insurance.entities.Policy;
import com.insurance.entities.User;
import com.insurance.entities.WithdrawalRequest;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.IWithdrawalService;
import com.insurance.repository.AdminRepository;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.PolicyRepository;
import com.insurance.repository.UserRepository;
import com.insurance.repository.WithdrawalRequestRepository;
import com.insurance.security.JwtTokenProvider;


@Service
public class WithdrawalService implements IWithdrawalService {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawalService.class);

    @Autowired
    WithdrawalRequestRepository withdrawalRequestRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PolicyRepository policyRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void withdrawalRequest(String token, String policy_id) {
        logger.info("Processing withdrawal request for policy ID: {}", policy_id);
        String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            logger.error("User not found: {}", username);
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();
        Customer customer = customerRepository.findByUser(user);
        if (customer == null || !customer.getUser().isActive()) {
            logger.error("Unauthorized withdrawal request for user: {}", username);
            throw new ApiException("Unauthorized");
        }

        Policy policy = policyRepository.findById(policy_id).orElse(null);
        if (policy == null) {
            logger.error("Policy not found: {}", policy_id);
            throw new ResourceNotFoundException("Policy not found");
        }

        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatus("PENDING");
        withdrawalRequest.setCustomer(customer);
        withdrawalRequest.setPolicy(policy);
        withdrawalRequest.setRequestDate(LocalDateTime.now());
        withdrawalRequestRepository.save(withdrawalRequest);

        logger.info("Withdrawal request created for customer: {} and policy: {}", customer.getCustomerId(), policy_id);
    }

    public String approveWithdrawal(String token, long withdrawal_id) {
        logger.info("Approving withdrawal request with ID: {}", withdrawal_id);
        String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            logger.error("User not found: {}", username);
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();
        Admin admin = adminRepository.findByUser(user);
        if (admin == null || !admin.getUser().isActive()) {
            logger.error("Unauthorized admin approval attempt by user: {}", username);
            throw new ApiException("Unauthorized");
        }

        Optional<WithdrawalRequest> oWithdrawalRequest = withdrawalRequestRepository.findById(withdrawal_id);
        if (oWithdrawalRequest.isEmpty()) {
            logger.error("Withdrawal request not found with ID: {}", withdrawal_id);
            throw new ResourceNotFoundException("Withdrawal Request not found");
        }

        WithdrawalRequest withdrawalRequest = oWithdrawalRequest.get();
        withdrawalRequest.setStatus("APPROVED");
        withdrawalRequest.setAdmin(admin);
        withdrawalRequestRepository.save(withdrawalRequest);

        logger.info("Withdrawal request ID: {} approved by admin: {}", withdrawal_id, admin.getAdminId());
        return "Withdrawal approved";
    }

    @Override
    public String rejectWithdrawal(String token, long withdrawal_id) {
        logger.info("Rejecting withdrawal request with ID: {}", withdrawal_id);
        String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            logger.error("User not found: {}", username);
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();
        Admin admin = adminRepository.findByUser(user);
        if (admin == null || !admin.getUser().isActive()) {
            logger.error("Unauthorized admin rejection attempt by user: {}", username);
            throw new ApiException("Unauthorized");
        }

        Optional<WithdrawalRequest> oWithdrawalRequest = withdrawalRequestRepository.findById(withdrawal_id);
        if (oWithdrawalRequest.isEmpty()) {
            logger.error("Withdrawal request not found with ID: {}", withdrawal_id);
            throw new ResourceNotFoundException("Withdrawal Request not found");
        }

        WithdrawalRequest withdrawalRequest = oWithdrawalRequest.get();
        withdrawalRequest.setStatus("REJECTED");
        withdrawalRequest.setAdmin(admin);
        withdrawalRequestRepository.save(withdrawalRequest);

        logger.info("Withdrawal request ID: {} rejected by admin: {}", withdrawal_id, admin.getAdminId());
        return "Withdrawal rejected";
    }
}
