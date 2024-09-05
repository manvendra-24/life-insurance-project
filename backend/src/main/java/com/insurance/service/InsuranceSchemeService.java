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

import com.insurance.entities.InsuranceScheme;
import com.insurance.entities.InsuranceType;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.IInsuranceSchemeService;
import com.insurance.repository.InsuranceSchemeRepository;
import com.insurance.repository.InsuranceTypeRepository;
import com.insurance.request.InsuranceSchemeRequest;
import com.insurance.response.InsuranceSchemeResponse;
import com.insurance.util.Mappers;
import com.insurance.util.PagedResponse;
import com.insurance.util.UniqueIdGenerator;

@Service
public class InsuranceSchemeService implements IInsuranceSchemeService {

    private static final Logger logger = LoggerFactory.getLogger(InsuranceSchemeService.class);

    @Autowired
    Mappers mappers;

    @Autowired
    InsuranceTypeRepository insuranceTypeRepository;

    @Autowired
    InsuranceSchemeRepository insuranceSchemeRepository;

    @Autowired
    UniqueIdGenerator uniqueIdGenerator;

    @Override
    public String createInsuranceScheme(InsuranceSchemeRequest schemeRequest) {
        logger.info("Creating new insurance scheme with name: {}", schemeRequest.getSchemeName());

        InsuranceScheme insuranceScheme = mappers.schemeRequestToScheme(schemeRequest);
        insuranceScheme.setInsuranceSchemeId(uniqueIdGenerator.generateUniqueId(InsuranceScheme.class));

        insuranceSchemeRepository.save(insuranceScheme);

        logger.info("Insurance scheme created successfully with ID: {}", insuranceScheme.getInsuranceSchemeId());
        return "Insurance Scheme Created";
    }

    @Override
    public String updateInsuranceScheme(String id, InsuranceSchemeRequest schemeRequest) {
        logger.info("Updating insurance scheme with ID: {}", id);

        Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(id);
        if (oInsuranceScheme.isEmpty()) {
            logger.warn("Insurance scheme not found with ID: {}", id);
            throw new ResourceNotFoundException("Insurance scheme not found");
        }

        InsuranceScheme insuranceScheme = oInsuranceScheme.get();
        insuranceScheme.setName(schemeRequest.getSchemeName());

        Optional<InsuranceType> insuranceType = insuranceTypeRepository.findById(schemeRequest.getInsuranceTypeId());
        if (insuranceType.isEmpty()) {
            logger.warn("Insurance Type not found with ID: {}", schemeRequest.getInsuranceTypeId());
            throw new ResourceNotFoundException("Insurance Type not found");
        }

        insuranceScheme.setInsuranceType(insuranceType.get());
        insuranceScheme.setNewRegistrationCommission(schemeRequest.getNewRegistrationCommission());
        insuranceScheme.setInstallmentPaymentCommission(schemeRequest.getInstallmentPaymentCommission());
        insuranceSchemeRepository.save(insuranceScheme);

        logger.info("Insurance scheme updated successfully with ID: {}", id);
        return "Insurance Scheme Updated";
    }

    @Override
    public String activateInsuranceScheme(String id) {
        logger.info("Activating insurance scheme with ID: {}", id);

        Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(id);
        if (oInsuranceScheme.isEmpty()) {
            logger.warn("Insurance scheme not found with ID: {}", id);
            throw new ResourceNotFoundException("Insurance scheme not found");
        }

        InsuranceScheme insuranceScheme = oInsuranceScheme.get();
        if (insuranceScheme.isActive()) {
            logger.warn("Insurance scheme is already active with ID: {}", id);
            throw new ApiException("Insurance scheme is already active");
        }

        insuranceScheme.setActive(true);
        insuranceSchemeRepository.save(insuranceScheme);

        logger.info("Insurance scheme activated successfully with ID: {}", id);
        return "Insurance Scheme activated";
    }

    @Override
    public String deleteInsuranceScheme(String id) {
        logger.info("Deactivating insurance scheme with ID: {}", id);

        Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(id);
        if (oInsuranceScheme.isEmpty()) {
            logger.warn("Insurance scheme not found with ID: {}", id);
            throw new ResourceNotFoundException("Insurance scheme not found");
        }

        InsuranceScheme insuranceScheme = oInsuranceScheme.get();
        if (!insuranceScheme.isActive()) {
            logger.warn("Insurance scheme is already inactive with ID: {}", id);
            throw new ApiException("Insurance scheme is already inactive");
        }

        insuranceScheme.setActive(false);
        insuranceSchemeRepository.save(insuranceScheme);

        logger.info("Insurance scheme deactivated successfully with ID: {}", id);
        return "Insurance Scheme deactivated";
    }

    @Override
    public PagedResponse<InsuranceSchemeResponse> getAllInsuranceSchemes(int page, int size, String sortBy, String direction) {
        logger.info("Fetching all insurance schemes with pagination - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<InsuranceScheme> insuranceSchemePage = insuranceSchemeRepository.findAll(pageable);
        List<InsuranceSchemeResponse> insuranceSchemeResponseList = insuranceSchemePage.getContent().stream()
                .map(mappers::insuranceSchemeToInsuranceSchemeResponse).collect(Collectors.toList());

        logger.info("Fetched {} insurance schemes", insuranceSchemeResponseList.size());
        return new PagedResponse<>(insuranceSchemeResponseList, insuranceSchemePage.getNumber(), insuranceSchemePage.getSize(),
                insuranceSchemePage.getTotalElements(), insuranceSchemePage.getTotalPages(), insuranceSchemePage.isLast());
    }
}
