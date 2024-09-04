package com.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.State;

public interface StateRepository  extends JpaRepository<State, String>{

}