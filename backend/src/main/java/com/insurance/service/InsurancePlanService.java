package com.insurance.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.insurance.entities.InsurancePlan;
import com.insurance.entities.InsuranceScheme;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.IInsurancePlanService;
import com.insurance.repository.InsurancePlanRepository;
import com.insurance.repository.InsuranceSchemeRepository;
import com.insurance.request.InsurancePlanRequest;
import com.insurance.response.InsurancePlanResponse;
import com.insurance.util.Mappers;
import com.insurance.util.PagedResponse;
import com.insurance.util.UniqueIdGenerator;

@Service
public class InsurancePlanService implements IInsurancePlanService{

	
	@Autowired
	InsurancePlanRepository insurancePlanRepository;
	
	@Autowired
	InsuranceSchemeRepository insuranceSchemeRepository;
	
	@Autowired
	UniqueIdGenerator uniqueIdGenerator;
	
	@Autowired
	Mappers mappers;
	
	
	@Override
	public String createInsurancePlan(InsurancePlanRequest planRequest) {
        Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(planRequest.getInsuranceSchemeId());
        if (oInsuranceScheme.isEmpty()) {
            throw new ResourceNotFoundException("Insurance Scheme not found");
        }
        InsurancePlan insurancePlan = mappers.insurancePlanRequestToInsurancePlan(planRequest, oInsuranceScheme.get());
        insurancePlan.setInsuranceId(uniqueIdGenerator.generateUniqueId(InsurancePlan.class));
        insurancePlanRepository.save(insurancePlan);
        return "Insurance Plan Created";
    }

	
	@Override
    public String updateInsurancePlan(String id, InsurancePlanRequest planRequest) {
        Optional<InsurancePlan> oInsurancePlan = insurancePlanRepository.findById(id);
        if (oInsurancePlan.isEmpty()) {
            throw new ResourceNotFoundException("Insurance Plan not found");
        }
        InsurancePlan insurancePlan = oInsurancePlan.get();

        Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(planRequest.getInsuranceSchemeId());
        if (oInsuranceScheme.isEmpty()) {
            throw new ResourceNotFoundException("Insurance Scheme not found");
        }
        insurancePlan.setInsuranceScheme(oInsuranceScheme.get());
        insurancePlan.setMinimumPolicyTerm(planRequest.getMinimumPolicyTerm());
        insurancePlan.setMaximumPolicyTerm(planRequest.getMaximumPolicyTerm());
        insurancePlan.setMinimumAge(planRequest.getMinimumAge());
        insurancePlan.setMaximumAge(planRequest.getMaximumAge());
        insurancePlan.setMinimumInvestmentAmount(planRequest.getMinimumInvestmentAmount());
        insurancePlan.setMaximumInvestmentAmount(planRequest.getMaximumInvestmentAmount());
        insurancePlan.setProfitRatio(planRequest.getProfitRatio());
        insurancePlanRepository.save(insurancePlan);
        return "Insurance Plan Updated";
    }

	
	@Override
    public String activateInsurancePlan(String id) {
        Optional<InsurancePlan> oInsurancePlan = insurancePlanRepository.findById(id);
        if (oInsurancePlan.isEmpty()) {
            throw new ResourceNotFoundException("Insurance Plan not found");
        }
        InsurancePlan insurancePlan = oInsurancePlan.get();
        if (insurancePlan.isActive()) {
            throw new ApiException("Insurance Plan is already active");
        }
        insurancePlan.setActive(true);
        insurancePlanRepository.save(insurancePlan);
        return "Insurance Plan Activated";
    }

	
	@Override
    public String deleteInsurancePlan(String id) {
        Optional<InsurancePlan> oInsurancePlan = insurancePlanRepository.findById(id);
        if (oInsurancePlan.isEmpty()) {
            throw new ResourceNotFoundException("Insurance Plan not found");
        }
        InsurancePlan insurancePlan = oInsurancePlan.get();
        if (!insurancePlan.isActive()) {
            throw new ApiException("Insurance Plan is already inactive");
        }
        insurancePlan.setActive(false);
        insurancePlanRepository.save(insurancePlan);
        return "Insurance Plan Deactivated";
    }

	
	@Override
    public PagedResponse<InsurancePlanResponse> getAllInsurancePlans(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<InsurancePlan> insurancePlanPage = insurancePlanRepository.findAll(pageable);
        List<InsurancePlanResponse> insurancePlanResponseList = insurancePlanPage.getContent().stream()
                .map(mappers::insurancePlanToInsurancePlanResponse).collect(Collectors.toList());

        return new PagedResponse<>(insurancePlanResponseList, insurancePlanPage.getNumber(), insurancePlanPage.getSize(),
                insurancePlanPage.getTotalElements(), insurancePlanPage.getTotalPages(), insurancePlanPage.isLast());
    }
  
  
  
  
  
  
  
  
  
}
