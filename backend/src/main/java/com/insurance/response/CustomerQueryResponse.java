package com.insurance.response;

import java.time.LocalDateTime;

import com.insurance.entities.Customer;
import com.insurance.entities.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CustomerQueryResponse {
	
	
	    private Long queryId;

	  
	    private String customerId;

	
	    private String subject;

	   
	    private String message;

	    private LocalDateTime submittedAt;

	    private String status;  
	    
	    private String response;
	 
	    private String employeeId;

}
