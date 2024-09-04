package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.InsuranceScheme;

public interface InsuranceSchemeRepository extends JpaRepository<InsuranceScheme, String>{

}
