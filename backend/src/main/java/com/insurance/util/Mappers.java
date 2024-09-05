package com.insurance.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.insurance.entities.Admin;
import com.insurance.entities.Agent;
import com.insurance.entities.City;
import com.insurance.entities.Employee;
import com.insurance.entities.InsurancePlan;
import com.insurance.entities.InsuranceScheme;
import com.insurance.entities.InsuranceType;
import com.insurance.entities.Policy;
import com.insurance.entities.State;
import com.insurance.entities.Transaction;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.repository.InsurancePlanRepository;
import com.insurance.repository.InsuranceTypeRepository;
import com.insurance.repository.StateRepository;
import com.insurance.request.CityRequest;
import com.insurance.request.InsurancePlanRequest;
import com.insurance.request.InsuranceSchemeRequest;
import com.insurance.request.InsuranceTypeRequest;
import com.insurance.request.StateRequest;
import com.insurance.response.AdminResponse;
import com.insurance.response.AgentResponse;
import com.insurance.response.CityResponse;
import com.insurance.response.CommissionResponse;
import com.insurance.response.EmployeeResponse;
import com.insurance.response.InsurancePlanResponse;
import com.insurance.response.InsuranceSchemeResponse;
import com.insurance.response.InsuranceTypeResponse;
import com.insurance.response.PolicyResponse;
import com.insurance.response.StateResponse;
import com.insurance.response.TransactionResponse;


@Component
public class Mappers {

	
	@Autowired
	InsuranceTypeRepository insuranceTypeRepository;
	
	@Autowired
	InsurancePlanRepository insurancePlanRepository;
	
	  @Autowired
	  StateRepository stateRepository;
	  

	


		public EmployeeResponse employeeToEmployeeResponse(Employee employee) {
		    EmployeeResponse employeeResponse = new EmployeeResponse();
		    employeeResponse.setEmployeeId(employee.getEmployeeId());
		    employeeResponse.setActive(employee.getUser().isActive());
		    employeeResponse.setEmail(employee.getUser().getEmail());
		    employeeResponse.setName(employee.getName());
		    employeeResponse.setUsername(employee.getUser().getUsername());
		    return employeeResponse;
		}


		public InsuranceScheme schemeRequestToScheme(InsuranceSchemeRequest schemeRequest) {
			InsuranceScheme insuranceScheme = new InsuranceScheme();
			insuranceScheme.setActive(true);
			insuranceScheme.setName(schemeRequest.getSchemeName());
			Optional<InsuranceType> insuranceType = insuranceTypeRepository.findById(schemeRequest.getInsuranceTypeId());
			if(insuranceType.isEmpty()) {
				throw new ResourceNotFoundException("Insurance Type not found");
			}
			insuranceScheme.setInsuranceType(insuranceType.get());
			insuranceScheme.setNewRegistrationCommission(schemeRequest.getNewRegistrationCommission());
			insuranceScheme.setInstallmentPaymentCommission(schemeRequest.getInstallmentPaymentCommission());	
			return insuranceScheme;
		}
		
		
		
		public InsuranceSchemeResponse insuranceSchemeToInsuranceSchemeResponse(InsuranceScheme insuranceScheme) {
		    InsuranceSchemeResponse response = new InsuranceSchemeResponse();
		    response.setInsuranceSchemeId(insuranceScheme.getInsuranceSchemeId());
		    response.setName(insuranceScheme.getName());
		    response.setNewRegistrationCommission(insuranceScheme.getNewRegistrationCommission());
		    response.setInstallmentPaymentCommission(insuranceScheme.getInstallmentPaymentCommission());
		    response.setDescription(insuranceScheme.getDescription());
		    response.setInsuranceType(insuranceScheme.getInsuranceType());
		    response.setActive(insuranceScheme.isActive());
		    return response;
		}

		
		public InsurancePlan insurancePlanRequestToInsurancePlan(InsurancePlanRequest request, InsuranceScheme insuranceScheme) {
	        InsurancePlan insurancePlan = new InsurancePlan();
	        insurancePlan.setInsuranceScheme(insuranceScheme);
	        insurancePlan.setMinimumPolicyTerm(request.getMinimumPolicyTerm());
	        insurancePlan.setMaximumPolicyTerm(request.getMaximumPolicyTerm());
	        insurancePlan.setMinimumAge(request.getMinimumAge());
	        insurancePlan.setMaximumAge(request.getMaximumAge());
	        insurancePlan.setMinimumInvestmentAmount(request.getMinimumInvestmentAmount());
	        insurancePlan.setMaximumInvestmentAmount(request.getMaximumInvestmentAmount());
	        insurancePlan.setProfitRatio(request.getProfitRatio());
	        insurancePlan.setActive(request.isActive());
	        insurancePlan.setCommission(request.getCommission());
	        return insurancePlan;
	    }
		
		

	    public InsurancePlanResponse insurancePlanToInsurancePlanResponse(InsurancePlan insurancePlan) {
	        InsurancePlanResponse response = new InsurancePlanResponse();
	        response.setInsuranceId(insurancePlan.getInsuranceId());
	        response.setInsuranceSchemeId(insurancePlan.getInsuranceScheme().getInsuranceSchemeId());
	        response.setInsuranceSchemeName(insurancePlan.getInsuranceScheme().getName());
	        response.setMinimumPolicyTerm(insurancePlan.getMinimumPolicyTerm());
	        response.setMaximumPolicyTerm(insurancePlan.getMaximumPolicyTerm());
	        response.setMinimumAge(insurancePlan.getMinimumAge());
	        response.setMaximumAge(insurancePlan.getMaximumAge());
	        response.setMinimumInvestmentAmount(insurancePlan.getMinimumInvestmentAmount());
	        response.setMaximumInvestmentAmount(insurancePlan.getMaximumInvestmentAmount());
	        response.setProfitRatio(insurancePlan.getProfitRatio());
	        response.setActive(insurancePlan.isActive());
	        response.setCommission(insurancePlan.getCommission());
	        return response;
	    }
	    
		  public State stateRequestToState(StateRequest stateRequest) {
		      State state=new State();
		      state.setName(stateRequest.getName());;
		      state.setActive(true);
		      return state;
		    }

		  public StateResponse stateToStateResponse(State state) {
			    StateResponse stateResponse = new StateResponse();
			    stateResponse.setStateId(state.getStateId());
			    stateResponse.setName(state.getName());
			    stateResponse.setActive(state.isActive());

			    // Map the list of cities
			    List<CityResponse> cityResponses = state.getCities().stream()
			        .map(this::cityToCityResponse)
			        .collect(Collectors.toList());

			    stateResponse.setCities(cityResponses);
			    return stateResponse;
			}

	   
			public City cityRequestToCity(CityRequest cityRequest) {
				
				State state = stateRepository.findById(cityRequest.getState_id())
		             .orElseThrow(() -> new IllegalArgumentException("Invalid state ID"));
				 City city = new City();
			        city.setName(cityRequest.getName());
			        city.setState(state);
			        city.setActive(true);
				return city;	
			}

			public CityResponse cityToCityResponse(City city) {
			    CityResponse cityResponse = new CityResponse();
			    cityResponse.setCityId(city.getCityId());
			    cityResponse.setName(city.getName());
			    cityResponse.setActive(city.isActive());
			    return cityResponse;
			}
			
			public  InsuranceTypeResponse  insuranceTypeToInsuranceTypeResponse(InsuranceType insuranceType) {
				InsuranceTypeResponse insuranceTypeResponse = new InsuranceTypeResponse();
				insuranceTypeResponse.setActive(insuranceType.isActive());
				insuranceTypeResponse.setName(insuranceType.getName());
				insuranceTypeResponse.setInsuranceTypeId(insuranceType.getInsuranceTypeId());
				return insuranceTypeResponse;
			}
			
			 public AgentResponse agentToAgentResponse(Agent agent) {
		          AgentResponse agentResponse = new AgentResponse();
		          agentResponse.setAgentId(agent.getAgentId());
		          agentResponse.setActive(agent.getUser().isActive());
		          agentResponse.setEmail(agent.getUser().getEmail());
		          agentResponse.setName(agent.getName());
		          agentResponse.setUsername(agent.getUser().getUsername());
		          return agentResponse;
		      }
			 public InsuranceType insuranceTypeRequestToInsuranceType(InsuranceTypeRequest typeRequest) {
				    InsuranceType insuranceType = new InsuranceType();
				    insuranceType.setName(typeRequest.getName());
				    insuranceType.setActive(true);
				    return insuranceType;
				  }

			 
			 public AdminResponse adminToAdminResponse(Admin admin) {
			        AdminResponse adminResponse = new AdminResponse();
			        adminResponse.setAdminId(admin.getAdminId());
			        adminResponse.setActive(admin.getUser().isActive());
			        adminResponse.setEmail(admin.getUser().getEmail());
			        adminResponse.setName(admin.getName());
			        adminResponse.setUsername(admin.getUser().getUsername());
			        return adminResponse;
			    }
			    
			    public PolicyResponse convertToPolicyResponse(Policy policy) {
			        PolicyResponse policyResponse = new PolicyResponse();
			        policyResponse.setPolicyId(policy.getPolicyId());
			        policyResponse.setPlan_id(policy.getPlan().getInsuranceId());
			        policyResponse.setStartDate(policy.getStartDate());
			        policyResponse.setEndDate(policy.getEndDate());
			        policyResponse.setPolicyTerm(policy.getPolicyTerm());
			        policyResponse.setTotalInvestmentAmount(policy.getTotalInvestmentAmount());
			        policyResponse.setPaymentInterval(policy.getPaymentInterval());
			        policyResponse.setInstallmentAmount(policy.getInstallmentAmount());
			        policyResponse.setTotalAmountPaid(policy.getTotalAmountPaid());
			        policyResponse.setNextPaymentDate(policy.getNextPaymentDate());

			        return policyResponse;
			    }
			    
			    public CommissionResponse convertToCommissionResponse(Policy policy) {
			        CommissionResponse commissionResponse = new CommissionResponse();
			        commissionResponse.setPolicyId(policy.getPolicyId());
			        commissionResponse.setPlan_id(policy.getPlan().getInsuranceId());
			        commissionResponse.setStartDate(policy.getStartDate());
			        commissionResponse.setEndDate(policy.getEndDate());
			        commissionResponse.setPolicyTerm(policy.getPolicyTerm());
			        commissionResponse.setTotalInvestmentAmount(policy.getTotalInvestmentAmount());
			        commissionResponse.setPaymentInterval(policy.getPaymentInterval());
			        commissionResponse.setCommission(policy.getPlan().getCommission());
			        return commissionResponse;
			    }
			    
			    public TransactionResponse transactionToTransactionResponse(Transaction transaction) {
				    TransactionResponse transactionResponse = new TransactionResponse();
				    transactionResponse.setTransactionId(transaction.getTransactionId());
				    transactionResponse.setPolicyId(transaction.getPolicy().getPolicyId());
				    transactionResponse.setTransactionType(transaction.getTransactionType().toString());
				    transactionResponse.setAmount(transaction.getAmount());
				    transactionResponse.setDate(transaction.getDate());
				    transactionResponse.setStatus(transaction.getStatus().toString());
				    return transactionResponse;
				}


	

}
