package com.insurance.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class InsurancePlanService implements IInsurancePlanService {

    private static final Logger logger = LoggerFactory.getLogger(InsurancePlanService.class);

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
        logger.info("Creating new insurance plan for scheme ID: {}", planRequest.getInsuranceSchemeId());

        Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(planRequest.getInsuranceSchemeId());
        if (oInsuranceScheme.isEmpty()) {
            logger.warn("Insurance Scheme not found for ID: {}", planRequest.getInsuranceSchemeId());
            throw new ResourceNotFoundException("Insurance Scheme not found");
        }
        
        InsurancePlan insurancePlan = mappers.insurancePlanRequestToInsurancePlan(planRequest, oInsuranceScheme.get());
        insurancePlan.setInsuranceId(uniqueIdGenerator.generateUniqueId(InsurancePlan.class));
        insurancePlanRepository.save(insurancePlan);

        logger.info("Insurance Plan created successfully with ID: {}", insurancePlan.getInsuranceId());
        return "Insurance Plan Created";
    }

    @Override
    public String updateInsurancePlan(String id, InsurancePlanRequest planRequest) {
        logger.info("Updating insurance plan with ID: {}", id);

        Optional<InsurancePlan> oInsurancePlan = insurancePlanRepository.findById(id);
        if (oInsurancePlan.isEmpty()) {
            logger.warn("Insurance Plan not found with ID: {}", id);
            throw new ResourceNotFoundException("Insurance Plan not found");
        }
        
        InsurancePlan insurancePlan = oInsurancePlan.get();

        Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(planRequest.getInsuranceSchemeId());
        if (oInsuranceScheme.isEmpty()) {
            logger.warn("Insurance Scheme not found for ID: {}", planRequest.getInsuranceSchemeId());
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

        logger.info("Insurance Plan updated successfully with ID: {}", id);
        return "Insurance Plan Updated";
    }

    @Override
    public String activateInsurancePlan(String id) {
        logger.info("Activating insurance plan with ID: {}", id);

        Optional<InsurancePlan> oInsurancePlan = insurancePlanRepository.findById(id);
        if (oInsurancePlan.isEmpty()) {
            logger.warn("Insurance Plan not found with ID: {}", id);
            throw new ResourceNotFoundException("Insurance Plan not found");
        }

        InsurancePlan insurancePlan = oInsurancePlan.get();
        if (insurancePlan.isActive()) {
            logger.warn("Insurance Plan is already active with ID: {}", id);
            throw new ApiException("Insurance Plan is already active");
        }

        insurancePlan.setActive(true);
        insurancePlanRepository.save(insurancePlan);

        logger.info("Insurance Plan activated successfully with ID: {}", id);
        return "Insurance Plan Activated";
    }

    @Override
    public String deleteInsurancePlan(String id) {
        logger.info("Deactivating insurance plan with ID: {}", id);

        Optional<InsurancePlan> oInsurancePlan = insurancePlanRepository.findById(id);
        if (oInsurancePlan.isEmpty()) {
            logger.warn("Insurance Plan not found with ID: {}", id);
            throw new ResourceNotFoundException("Insurance Plan not found");
        }

        InsurancePlan insurancePlan = oInsurancePlan.get();
        if (!insurancePlan.isActive()) {
            logger.warn("Insurance Plan is already inactive with ID: {}", id);
            throw new ApiException("Insurance Plan is already inactive");
        }

        insurancePlan.setActive(false);
        insurancePlanRepository.save(insurancePlan);

        logger.info("Insurance Plan deactivated successfully with ID: {}", id);
        return "Insurance Plan Deactivated";
    }

    @Override
    public PagedResponse<InsurancePlanResponse> getAllInsurancePlans(int page, int size, String sortBy, String direction) {
        logger.info("Fetching all insurance plans with pagination - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<InsurancePlan> insurancePlanPage = insurancePlanRepository.findAll(pageable);
        List<InsurancePlanResponse> insurancePlanResponseList = insurancePlanPage.getContent().stream()
                .map(mappers::insurancePlanToInsurancePlanResponse).collect(Collectors.toList());

        logger.info("Fetched {} insurance plans", insurancePlanResponseList.size());
        return new PagedResponse<>(insurancePlanResponseList, insurancePlanPage.getNumber(), insurancePlanPage.getSize(),
                insurancePlanPage.getTotalElements(), insurancePlanPage.getTotalPages(), insurancePlanPage.isLast());
    }
}
