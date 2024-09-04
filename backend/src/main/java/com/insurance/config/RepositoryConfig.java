package com.insurance.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.Admin;
import com.insurance.entities.Agent;
import com.insurance.entities.City;
import com.insurance.entities.Customer;
import com.insurance.entities.Document;
import com.insurance.entities.Employee;
import com.insurance.entities.State;
import com.insurance.entities.User;
import com.insurance.repository.AdminRepository;
import com.insurance.repository.AgentRepository;
import com.insurance.repository.CityRepository;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.DocumentRepository;
import com.insurance.repository.EmployeeRepository;
import com.insurance.repository.InsurancePlanRepository;
import com.insurance.repository.InsuranceSchemeRepository;
import com.insurance.repository.InsuranceTypeRepository;
import com.insurance.repository.StateRepository;
import com.insurance.repository.UserRepository;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class RepositoryConfig {

    public Map<Class<?>, JpaRepository<?, String>> repositories(UserRepository userRepository, 
                                                              CustomerRepository customerRepository, 
                                                              AdminRepository adminRepository,
                                                              EmployeeRepository employeeRepository,
                                                              AgentRepository agentRepository,
                                                              CityRepository cityRepository,
                                                              StateRepository stateRepository,
                                                              InsurancePlanRepository insurancePlanRepository,
    														  InsuranceTypeRepository insuranceTypeRepository,
    														  InsuranceSchemeRepository insuranceSchemeRepository,
    														  DocumentRepository documentRepository){
        return Stream.of(
                Map.entry(User.class, userRepository),
                Map.entry(Customer.class, customerRepository),
                Map.entry(Admin.class, adminRepository),
                Map.entry(Employee.class, employeeRepository),
                Map.entry(Agent.class, agentRepository),
                Map.entry(City.class, cityRepository),
                Map.entry(State.class, stateRepository),
                Map.entry(InsurancePlanRepository.class, insurancePlanRepository),
                Map.entry(InsuranceTypeRepository.class, insuranceTypeRepository),
                Map.entry(InsuranceSchemeRepository.class, insuranceSchemeRepository),
                Map.entry(Document.class, documentRepository)

        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
}
