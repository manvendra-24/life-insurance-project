package com.insurance.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insurance.entities.Admin;
import com.insurance.entities.InsuranceSetting;
import com.insurance.entities.TaxSetting;
import com.insurance.entities.User;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.IAdminService;
import com.insurance.repository.AdminRepository;
import com.insurance.repository.AgentRepository;
import com.insurance.repository.CityRepository;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.EmployeeRepository;
import com.insurance.repository.InsurancePlanRepository;
import com.insurance.repository.InsuranceSchemeRepository;
import com.insurance.repository.InsuranceSettingRepository;
import com.insurance.repository.InsuranceTypeRepository;
import com.insurance.repository.RoleRepository;
import com.insurance.repository.StateRepository;
import com.insurance.repository.TaxSettingRepository;
import com.insurance.repository.TransactionRepository;
import com.insurance.repository.UserRepository;
import com.insurance.repository.WithdrawalRequestRepository;
import com.insurance.request.AdminRegisterRequest;
import com.insurance.request.InsuranceSettingRequest;
import com.insurance.request.TaxSettingRequest;
import com.insurance.response.AdminResponse;
import com.insurance.security.JwtTokenProvider;
import com.insurance.util.Mappers;
import com.insurance.util.PagedResponse;
import com.insurance.util.UniqueIdGenerator;


@Service
public class AdminService implements IAdminService{
	
	Mappers mappers;
	PasswordEncoder passwordEncoder;
	JwtTokenProvider jwtTokenProvider;
	
	public AdminService(Mappers mappers, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		super();
		this.mappers = mappers;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	InsuranceTypeRepository insuranceTypeRepository;
	
	@Autowired
	InsurancePlanRepository insurancePlanRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	AgentRepository agentRepository;
	
	@Autowired
	StateRepository stateRepository;
	
	@Autowired
	CityRepository cityRepository;
	
	@Autowired
	WithdrawalRequestRepository withdrawalRequestRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	

	@Autowired
	TaxSettingRepository taxSettingRepository;
	
	@Autowired
	InsuranceSchemeRepository insuranceSchemeRepository;
	
	@Autowired
	UniqueIdGenerator uniqueIdGenerator;

	
	@Autowired
	InsuranceSettingRepository insuranceSettingRepository;

	
	
	
	

  
	

		

		

		

		@Override
		public String createTaxSetting(TaxSettingRequest taxSettingRequest) {
			TaxSetting taxSetting=new TaxSetting();
		    taxSetting.setTaxPercentage(taxSettingRequest.getTaxPercentage());
		    taxSetting.setUpdatedAt(LocalDateTime.now());
		    taxSettingRepository.save(taxSetting);
		    return "Tax Setting created";
		}

		@Override
	    public String createInsuranceSetting(InsuranceSettingRequest insuranceSettingRequestDto) {
	      InsuranceSetting insuranceSetting=new InsuranceSetting();
	      insuranceSetting.setClaimDeduction(insuranceSettingRequestDto.getClaimDeduction());
	      insuranceSetting.setPenaltyAmount(insuranceSettingRequestDto.getPenaltyAmount());
	      insuranceSettingRepository.save(insuranceSetting);
	      return "Insurance Setting updated";

	    }
		
		
		@Override
	    public PagedResponse<AdminResponse> getAllAdmins(int page, int size, String sortBy, String direction) {
	        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) 
	                    ? Sort.by(sortBy).descending() 
	                    : Sort.by(sortBy).ascending();
	        PageRequest pageable = PageRequest.of(page, size, sort);
	        Page<Admin> page1 = adminRepository.findAll(pageable);
	        List<Admin> admins = page1.getContent();
	        List<AdminResponse> adminResponses = new ArrayList<>();
	        for (Admin admin : admins) {
	            AdminResponse adminResponse = mappers.adminToAdminResponse(admin);
	            adminResponses.add(adminResponse);
	        }
	        
	        return new PagedResponse<>(adminResponses, page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
	    }
	    
	    @Override
	    public String updateAdmin(String admin_id, AdminRegisterRequest adminRequest) {
	        Optional<Admin> oAdmin = adminRepository.findById(admin_id);
	        if (oAdmin.isEmpty()) {
	            throw new ResourceNotFoundException("Admin not found");
	        }

	        Admin admin = oAdmin.get();
	        admin.setName(adminRequest.getName());
	        admin.setPhoneNumber(adminRequest.getPhoneNumber());
	        User user = admin.getUser();
	        user.setEmail(adminRequest.getEmail());
	        user.setUsername(adminRequest.getUsername());
	        user.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
	        user.setUpdatedAt(LocalDateTime.now());
	      
	        userRepository.save(user);
	        admin.setUser(user);
	        adminRepository.save(admin);

	        return "Admin updated";
	    }

	    @Override
	    public String deleteAdmin(String admin_id) {
	        Optional<Admin> oAdmin = adminRepository.findById(admin_id);
	        if (oAdmin.isEmpty()) {
	            throw new ResourceNotFoundException("Admin not found");
	        }
	        Admin admin = oAdmin.get();
	        User user = admin.getUser();
	        if (!user.isActive()) {
	            throw new ApiException("User is already inactive");
	        }
	        user.setActive(false);
	        userRepository.save(user);
	        adminRepository.save(admin);
	        return "Admin successfully deleted";
	    }

	    @Override
	    public String activateAdmin(String admin_id) {
	        Optional<Admin> oAdmin = adminRepository.findById(admin_id);
	        if (oAdmin.isEmpty()) {
	            throw new ResourceNotFoundException("Admin not found");
	        }
	        Admin admin = oAdmin.get();
	        User user = admin.getUser();
	        if (user.isActive()) {
	            throw new ApiException("User is already active");
	        }
	        user.setActive(true);
	        userRepository.save(user);
	        adminRepository.save(admin);
	        return "Admin successfully activated";
	    }
		  


}
