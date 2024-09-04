package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.Admin;
import com.insurance.entities.User;

public interface AdminRepository extends JpaRepository<Admin, String>{

	Admin findByUser(User user);

}
