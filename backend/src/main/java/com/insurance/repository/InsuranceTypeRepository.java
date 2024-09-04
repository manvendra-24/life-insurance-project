package com.insurance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.InsuranceType;


public interface InsuranceTypeRepository extends JpaRepository<InsuranceType, String> {

	Optional<InsuranceType> findByInsuranceTypeId(String insurance_type_id);

}