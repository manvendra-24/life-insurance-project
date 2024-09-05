package com.insurance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.Customer;
import com.insurance.entities.User;

public interface CustomerRepository extends JpaRepository<Customer,String >{

	Customer findByUser(User user);

}
