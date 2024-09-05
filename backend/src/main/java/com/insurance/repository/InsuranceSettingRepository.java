package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.InsuranceSetting;

public interface InsuranceSettingRepository extends JpaRepository<InsuranceSetting, Long>{

}
