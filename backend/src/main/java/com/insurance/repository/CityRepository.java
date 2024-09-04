package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.City;

public interface CityRepository extends JpaRepository<City, String>{

	
}
