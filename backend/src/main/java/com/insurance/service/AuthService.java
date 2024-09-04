package com.insurance.service;

import com.insurance.entities.Admin;
import com.insurance.entities.Agent;
import com.insurance.entities.Customer;
import com.insurance.entities.Employee;
import com.insurance.entities.Role;
import com.insurance.entities.User;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.IAuthService;
import com.insurance.repository.AdminRepository;
import com.insurance.repository.AgentRepository;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.EmployeeRepository;
import com.insurance.repository.RoleRepository;
import com.insurance.repository.UserRepository;
import com.insurance.request.LoginDto;
import com.insurance.request.ProfileRequest;
import com.insurance.request.AdminRegisterRequest;
import com.insurance.security.JwtTokenProvider;
import com.insurance.util.UniqueIdGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthService(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           AdminRepository adminRepository) {
    	this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminRepository = adminRepository;
    }

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    AgentRepository agentRepository;
    
    @Autowired
    EmployeeRepository employeeRepository;
    
    @Autowired
	UniqueIdGenerator uniqueIdGenerator;
    
    @Override
    public String login(LoginDto loginDto) {
    	String usernameOrEmail = loginDto.getUsernameOrEmail();
    	String password = loginDto.getPassword();
    	
    	UsernamePasswordAuthenticationToken temp = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);
        Authentication authentication = authenticationManager.authenticate(temp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).get();
        
        if(!user.isActive()) {
        	throw new ResourceNotFoundException("User does not exists");
        }
        return token;
    }

    
    
    @Override
    public String registerAdmin(AdminRegisterRequest registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ApiException("Username already exists!");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ApiException("Email already exists!");
        }

        User user = new User();
        user.setUserId(uniqueIdGenerator.generateUniqueId(User.class));
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setUserId(uniqueIdGenerator.generateUniqueId(User.class));
        Optional<Role> oRole = roleRepository.findByName("Role_Admin");
        if(oRole.isEmpty()) {
        	throw new ResourceNotFoundException("Role not found");
        }
        user.setRole(oRole.get());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Admin admin = new Admin();
        admin.setAdminId(uniqueIdGenerator.generateUniqueId(Admin.class));
        admin.setName(registerDto.getName());
        admin.setPhoneNumber(registerDto.getPhoneNumber());
        userRepository.save(user);
        admin.setUser(user);
        adminRepository.save(admin);
        
        return "Admin registered successfully!";
    }

	

	@Override
	public String getRole(String token) {
		String username = jwtTokenProvider.getUsername(token);
		Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
		if(oUser.isEmpty()) {
			throw new ResourceNotFoundException("user not found");
		}
		User user = oUser.get();
		return user.getRole().getName();
	}
	
	
	@Override
	  public String profileUpdate(String token, ProfileRequest profileRequest) {
	      String username = jwtTokenProvider.getUsername(token);
	      Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
	      if (oUser.isEmpty()) {
	          throw new ResourceNotFoundException("User is not available");
	      }
	      User user = oUser.get();
	      String role = user.getRole().getName();
	      
	     
	      if (role.equalsIgnoreCase("role_admin")) {
	          Admin admin = adminRepository.findByUser(user);
	          admin.setName(profileRequest.getName());
	          adminRepository.save(admin); 
	      }
	      
	      else if (role.equalsIgnoreCase("role_employee")) {
	          Employee employee = employeeRepository.findByUser(user).get();
	          employee.setName(profileRequest.getName());
	          employeeRepository.save(employee); 
	      }
	      
	      else if (role.equalsIgnoreCase("role_agent")) {
	          Agent agent = agentRepository.findByUser(user);
	          agent.setName(profileRequest.getName());
	          agentRepository.save(agent); 
	      }
	      
	      
	      else if (role.equalsIgnoreCase("role_customer")) {
	          Customer customer = customerRepository.findByUser(user);
	          customer.setName(profileRequest.getName());
	          customerRepository.save(customer); 
	      }
	      
	      user.setUsername(profileRequest.getUsername());
	      user.setEmail(profileRequest.getEmail());
	      user.setPassword(profileRequest.getPassword());
	      
	      userRepository.save(user); 
	      
	      return "Profile updated successfully"; 
	  }

	
	
}
