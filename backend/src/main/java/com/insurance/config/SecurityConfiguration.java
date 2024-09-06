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
              .requestMatchers("/swagger-ui/", "/v3/api-docs/", "/v3/api-docs.yaml", 
                               "/swagger-resources/", "/swagger-ui.html", "/webjars/").permitAll()
              //authentication end point
              .requestMatchers("/SecureLife.com/login").permitAll()
              .requestMatchers("/SecureLife.com/profile/**").permitAll()
              .requestMatchers("/SecureLife.com/password/**").permitAll()
              
              //forgot password end points
              .requestMatchers("/SecureLife.com/otp/**").permitAll()
              
              //admin end points
              .requestMatchers("/SecureLife.com/admin/register").permitAll()
              .requestMatchers("/SecureLife.com/admins/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/admin/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/insurance-settings/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/tax-settings/**").hasRole("ADMIN")
              
              //agent end points
              .requestMatchers("/SecureLife.com/agents/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/agent/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/agents/**").hasRole("EMPLOYEE")
              .requestMatchers("/SecureLife.com/agent/**").hasRole("EMPLOYEE")
              
              //city end points
              .requestMatchers("/SecureLife.com/cities/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/city/**").hasRole("ADMIN")
              
              //state end points
              .requestMatchers("/SecureLife.com/states/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/state/**").hasRole("ADMIN")
              
              //employee end points
              .requestMatchers("/SecureLife.com/employees/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/employee/**").hasRole("ADMIN")
              
              //insurance type end points
              .requestMatchers("/SecureLife.com/types/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/type/**").hasRole("ADMIN")
              
              //insurance plan end points
              .requestMatchers("/SecureLife.com/plans/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/plan/**").hasRole("ADMIN")
              
              //insurance scheme end points
              .requestMatchers("/SecureLife.com/schemes/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/scheme/**").hasRole("ADMIN")
              
              //customer end points
              .requestMatchers("/SecureLife.com/customer/register").hasRole("AGENT")
              .requestMatchers("/SecureLife.com/customers/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/customer/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/customers/**").hasRole("EMPLOYEE")
              .requestMatchers("/SecureLife.com/customer/**").hasRole("EMPLOYEE")
              
              //documents end point
              .requestMatchers("/SecureLife.com/document/upload").hasRole("CUSTOMER")
              .requestMatchers("/SecureLife.com//document/{document_id}/download").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com//document/{document_id}/download").hasRole("ADMIN")

              //withdrawal end points
              .requestMatchers("/SecureLife.com/policy/{policy_id}/withdrawal").hasRole("CUSTOMER")
              .requestMatchers("/SecureLife.com/withdrawals/**").hasRole("ADMIN")
              
              //queries end points
              .requestMatchers("/SecureLife.com/queries/ask").hasRole("CUSTOMER")
              .requestMatchers("/SecureLife.com/query/**").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/queries").hasRole("CUSTOMER")
              .requestMatchers("/SecureLife.com/queries").hasRole("ADMIN")
              
              //policy end points
              .requestMatchers("/SecureLife.com/mypolicies").hasRole("CUSTOMER")
              .requestMatchers("/SecureLife.com/mycommissions").hasRole("CUSTOMER")
              .requestMatchers("/SecureLife.com/policies/register").hasRole("CUSTOMER")
              .requestMatchers("/SecureLife.com/policies").hasRole("ADMIN")
              .requestMatchers("/SecureLife.com/policies").hasRole("EMPLOYEE")



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