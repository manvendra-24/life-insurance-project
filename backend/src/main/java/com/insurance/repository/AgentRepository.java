package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.Agent;
import com.insurance.entities.User;

public interface AgentRepository extends JpaRepository<Agent, String>{

	Agent findByUser(User user);

}
