package com.insurance.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class InsuranceSchemeService implements IInsuranceSchemeService{

	
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
	  	InsuranceScheme insuranceScheme = mappers.schemeRequestToScheme(schemeRequest);
        insuranceScheme.setInsuranceSchemeId(uniqueIdGenerator.generateUniqueId(InsuranceScheme.class));

	  	insuranceSchemeRepository.save(insuranceScheme);
	  	return "Insurance Scheme Created";
	  }

	  @Override
	  public String updateInsuranceScheme(String id, InsuranceSchemeRequest schemeRequest) {
	  	Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(id);
	  	if(oInsuranceScheme.isEmpty()) {
	  		throw new ResourceNotFoundException("Insurance scheme not found");
	  	}
	  	InsuranceScheme insuranceScheme = oInsuranceScheme.get();
	  	insuranceScheme.setName(schemeRequest.getSchemeName());
	  	Optional<InsuranceType> insuranceType = insuranceTypeRepository.findById(schemeRequest.getInsuranceTypeId());
	  	if(insuranceType.isEmpty()) {
	  		throw new ResourceNotFoundException("Insurance Type not found");
	  	}
	  	insuranceScheme.setInsuranceType(insuranceType.get());
	  	insuranceScheme.setNewRegistrationCommission(schemeRequest.getNewRegistrationCommission());
	  	insuranceScheme.setInstallmentPaymentCommission(schemeRequest.getInstallmentPaymentCommission());
	  	insuranceSchemeRepository.save(insuranceScheme);
	  	return "Insurance Scheme Updated";
	  }

	  @Override
	  public String activateInsuranceScheme(String id) {
	  	Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(id);
	  	if(oInsuranceScheme.isEmpty()) {
	  		throw new ResourceNotFoundException("Insurance scheme not found");
	  	}
	  	InsuranceScheme insuranceScheme = oInsuranceScheme.get();
	  	if(insuranceScheme.isActive()) {
	  		throw new ApiException("Insurance scheme is already active");
	  	}
	  	insuranceScheme.setActive(true);
	  	insuranceSchemeRepository.save(insuranceScheme);
	  	return "Insurance Scheme activated";
	  }

	  @Override
	  public String deleteInsuranceScheme(String id) {
	      Optional<InsuranceScheme> oInsuranceScheme = insuranceSchemeRepository.findById(id);
	      if (oInsuranceScheme.isEmpty()) {
	          throw new ResourceNotFoundException("Insurance scheme not found");
	      }
	      InsuranceScheme insuranceScheme = oInsuranceScheme.get();
	      if (!insuranceScheme.isActive()) {
	          throw new ApiException("Insurance scheme is already inactive");
	      }
	      insuranceScheme.setActive(false);
	      insuranceSchemeRepository.save(insuranceScheme);
	      return "Insurance Scheme deactivated";
	  }

	  @Override
	  public PagedResponse<InsuranceSchemeResponse> getAllInsuranceSchemes(int page, int size, String sortBy, String direction) {
	      Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
	          : Sort.by(sortBy).ascending();
	      PageRequest pageable = PageRequest.of(page, size, sort);

	      Page<InsuranceScheme> insuranceSchemePage = insuranceSchemeRepository.findAll(pageable);
	      List<InsuranceSchemeResponse> insuranceSchemeResponseList = insuranceSchemePage.getContent().stream()
	          .map(mappers::insuranceSchemeToInsuranceSchemeResponse).collect(Collectors.toList());

	      return new PagedResponse<>(insuranceSchemeResponseList, insuranceSchemePage.getNumber(), insuranceSchemePage.getSize(),
	          insuranceSchemePage.getTotalElements(), insuranceSchemePage.getTotalPages(), insuranceSchemePage.isLast());
	  }
}
