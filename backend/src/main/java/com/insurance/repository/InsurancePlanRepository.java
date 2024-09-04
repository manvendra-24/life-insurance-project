package com.insurance.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.InsurancePlan;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, String>{


}
