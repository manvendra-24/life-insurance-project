package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.WithdrawalRequest;

public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, Long>{
	
}
