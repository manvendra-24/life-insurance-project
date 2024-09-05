package com.insurance.service;

import com.insurance.entities.Agent;
import com.insurance.entities.Customer;
import com.insurance.entities.Policy;
import com.insurance.entities.User;
import com.insurance.enums.CreationStatusType;
import com.insurance.enums.PaymentInterval;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.repository.AgentRepository;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.InsurancePlanRepository;
import com.insurance.repository.PolicyRepository;
import com.insurance.repository.UserRepository;
import com.insurance.request.PolicyRequest;
import com.insurance.response.CommissionResponse;
import com.insurance.response.PolicyResponse;
import com.insurance.security.JwtTokenProvider;
import com.insurance.util.Mappers;
import com.insurance.util.PagedResponse;
import com.insurance.util.UniqueIdGenerator;
import com.insurance.interfaces.IPolicyService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PolicyService implements IPolicyService {

	
	@Autowired
	Mappers mappers;
	
	@Autowired
    private PolicyRepository policyRepository;

	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	UniqueIdGenerator uniqueIdGenerator;
	
	@Autowired
	AgentRepository agentRepository;

	@Autowired
	InsurancePlanRepository insurancePlanRepository;

    @Override
    public String createPolicy(String token, PolicyRequest policyRequest) {
        String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
           // logger.warn("User not available for token: {}", token);
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();

        Customer customer = customerRepository.findByUser(user);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        if (customer.getStatus() == CreationStatusType.REJECTED) {
            throw new ApiException("Sorry, you cannot create a policy");
        }

        Policy policy = new Policy();
        policy.setPolicyId(uniqueIdGenerator.generateUniqueId(Policy.class));
        policy.setCustomer(customer);
        policy.setPlan(insurancePlanRepository.findById(policyRequest.getPlan_id())
            .orElseThrow(() -> new ResourceNotFoundException("Plan not found")));
        policy.setStartDate(LocalDate.now());
        policy.setPolicyTerm(policyRequest.getPolicyTerm());
        policy.setEndDate(policy.getStartDate().plusYears(policy.getPolicyTerm()));
        policy.setTotalInvestmentAmount(policyRequest.getTotalInvestmentAmount());
        policy.setPaymentInterval(policyRequest.getPaymentInterval());
        policy.setInstallmentAmount(calculateInstallmentAmount(policyRequest));
        policy.setTotalAmountPaid(0);
        policy.setNextPaymentDate(policy.getStartDate());

        if (policyRequest.getAgent_id() != null) {
            Agent agent = agentRepository.findById(policyRequest.getAgent_id())
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));
            policy.setAgent(agent);
        }

        policyRepository.save(policy);

        return "Customer successfully registered for policy";
    }

    @Override
    public PagedResponse<PolicyResponse> getAllPolicies(String token, Pageable pageable) {
    	String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
           // logger.warn("User not available for token: {}", token);
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();
        //logger.info("User " + user.getUserId() + " trying to get policies of customer " + customerId);
    	
    	Page<Policy> policies = policyRepository.findAll(pageable);
        
        List<PolicyResponse> policyResponses = policies.getContent().stream()
            .map(mappers::convertToPolicyResponse)
            .collect(Collectors.toList());

        PagedResponse<PolicyResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(policyResponses);
        pagedResponse.setPage(policies.getNumber());
        pagedResponse.setSize(policies.getSize());
        pagedResponse.setTotalElements(policies.getTotalElements());
        pagedResponse.setTotalPages(policies.getTotalPages());
        pagedResponse.setLast(policies.isLast());

        return pagedResponse;
    }

    @Override
    public PagedResponse<PolicyResponse> getPoliciesByCustomerId(String token, String customerId, Pageable pageable) {
    	String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
           // logger.warn("User not available for token: {}", token);
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();
        //logger.info("User " + user.getUserId() + " trying to get policies of customer " + customerId);
    	Optional<Customer> customer = customerRepository.findById(customerId);
    	if(customer.isEmpty()) {
    		throw new ResourceNotFoundException("Customer not found");
    	}
    	Page<Policy> policies = policyRepository.findByCustomer(customer.get(), pageable);
        
        List<PolicyResponse> policyResponses = policies.getContent().stream()
            .map(mappers::convertToPolicyResponse)
            .collect(Collectors.toList());

        PagedResponse<PolicyResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(policyResponses);
        pagedResponse.setPage(policies.getNumber());
        pagedResponse.setSize(policies.getSize());
        pagedResponse.setTotalElements(policies.getTotalElements());
        pagedResponse.setTotalPages(policies.getTotalPages());
        pagedResponse.setLast(policies.isLast());

        return pagedResponse;
    }

    @Override
    public PagedResponse<PolicyResponse> getMyPolicies(String token, Pageable pageable) {
        String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();

        Customer customer = customerRepository.findByUser(user);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        if (customer.getStatus() == CreationStatusType.REJECTED) {
            throw new ApiException("Sorry, you cannot get your policies");
        }

        Page<Policy> policies = policyRepository.findByCustomer(customer, pageable);
        
        List<PolicyResponse> policyResponses = policies.getContent().stream()
            .map(mappers::convertToPolicyResponse)
            .collect(Collectors.toList());

        PagedResponse<PolicyResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(policyResponses);
        pagedResponse.setPage(policies.getNumber());
        pagedResponse.setSize(policies.getSize());
        pagedResponse.setTotalElements(policies.getTotalElements());
        pagedResponse.setTotalPages(policies.getTotalPages());
        pagedResponse.setLast(policies.isLast());

        return pagedResponse;
    }

    
    private int calculateInstallmentAmount(PolicyRequest policyRequest) {
        int totalInvestmentAmount = policyRequest.getTotalInvestmentAmount();
        int policyTermInYears = policyRequest.getPolicyTerm();
        PaymentInterval paymentInterval = policyRequest.getPaymentInterval();

        int paymentsPerYear;
        switch (paymentInterval) {
            case YEARLY:
                paymentsPerYear = 1;
                break;
            case HALF_YEARLY:
                paymentsPerYear = 2;
                break;
            case QUARTERLY:
                paymentsPerYear = 4;
                break;
            default:
                throw new IllegalArgumentException("Invalid payment interval");
        }

        int totalPayments = policyTermInYears * paymentsPerYear;
        return totalInvestmentAmount / totalPayments;
    }

	@Override
	public PagedResponse<CommissionResponse> getMyCommission(String token, Pageable pageable) {
		String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();

        Agent agent = agentRepository.findByUser(user);
        if (agent == null) {
            throw new ApiException("Agent not found");
        }
        

        Page<Policy> policies = policyRepository.findByAgent(agent, pageable);
        
        List<CommissionResponse> commissionResponses = policies.getContent().stream()
            .map(mappers::convertToCommissionResponse)
            .collect(Collectors.toList());

        PagedResponse<CommissionResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(commissionResponses);
        pagedResponse.setPage(policies.getNumber());
        pagedResponse.setSize(policies.getSize());
        pagedResponse.setTotalElements(policies.getTotalElements());
        pagedResponse.setTotalPages(policies.getTotalPages());
        pagedResponse.setLast(policies.isLast());

        return pagedResponse;
	}

	@Override
	public PagedResponse<CommissionResponse> getCommissionByAgentId(String token, String agentId, Pageable pageable) {
		String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();

        Optional<Agent> agent = agentRepository.findById(agentId);
        if (agent.isEmpty()) {
            throw new ApiException("Agent not found");
        }
        
        
        Page<Policy> policies = policyRepository.findByAgent(agent.get(), pageable);
        
        List<CommissionResponse> commissionResponses = policies.getContent().stream()
                .map(mappers::convertToCommissionResponse)
                .collect(Collectors.toList());

            PagedResponse<CommissionResponse> pagedResponse = new PagedResponse<>();
            pagedResponse.setContent(commissionResponses);
            pagedResponse.setPage(policies.getNumber());
            pagedResponse.setSize(policies.getSize());
            pagedResponse.setTotalElements(policies.getTotalElements());
            pagedResponse.setTotalPages(policies.getTotalPages());
            pagedResponse.setLast(policies.isLast());

            return pagedResponse;
	}


}
