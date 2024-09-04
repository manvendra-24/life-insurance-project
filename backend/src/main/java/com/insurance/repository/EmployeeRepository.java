package com.insurance.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.Employee;
import com.insurance.entities.User;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

	Optional<Employee> findByUser(User user2);


}
