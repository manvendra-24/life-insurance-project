package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.TaxSetting;

public interface TaxSettingRepository extends JpaRepository<TaxSetting, Long>{

}
