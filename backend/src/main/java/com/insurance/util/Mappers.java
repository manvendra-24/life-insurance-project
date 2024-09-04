package com.insurance.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.insurance.entities.Employee;
import com.insurance.entities.InsurancePlan;
import com.insurance.entities.InsuranceScheme;
import com.insurance.entities.InsuranceType;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.repository.InsurancePlanRepository;
import com.insurance.repository.InsuranceTypeRepository;
import com.insurance.request.InsurancePlanRequest;
import com.insurance.request.InsuranceSchemeRequest;
import com.insurance.response.EmployeeResponse;
import com.insurance.response.InsurancePlanResponse;
import com.insurance.response.InsuranceSchemeResponse;


@Component
public class Mappers {

	
	@Autowired
	InsuranceTypeRepository insuranceTypeRepository;
	
	@Autowired
	InsurancePlanRepository insurancePlanRepository;

	


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
	        return response;
	    }
	

}
