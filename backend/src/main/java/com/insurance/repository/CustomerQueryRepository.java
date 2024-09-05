package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.CustomerQuery;

public interface CustomerQueryRepository extends JpaRepository<CustomerQuery,Long>{

}
