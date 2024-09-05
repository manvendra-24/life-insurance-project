package com.insurance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.insurance.security.JwtAuthenticationEntryPoint;
import com.insurance.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	private JwtAuthenticationFilter authenticationFilter;

	public SecurityConfiguration(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.authenticationFilter = authenticationFilter;
	}
	
	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(authorize -> authorize
	            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml", 
	                             "/swagger-resources/**", "/swagger-ui.html", "/webjars/**").permitAll()
	            .requestMatchers("/SecureLife.com/login").permitAll()
	            .requestMatchers("/SecureLife.com/profile").permitAll()
	            .requestMatchers("/SecureLife.com/password").permitAll()
	            .requestMatchers("/SecureLife.com/otp").permitAll()
	            .anyRequest().authenticated()
	        )
	        .exceptionHandling(exception -> exception
	            .authenticationEntryPoint(authenticationEntryPoint)
	        )
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        );

	    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}


}
