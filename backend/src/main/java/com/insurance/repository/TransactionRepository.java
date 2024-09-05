package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{

}
