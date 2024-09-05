package com.insurance.request;

import java.time.LocalDateTime;

import com.insurance.entities.Customer;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CustomerQueryRequest {
	
	

	   	@NotBlank(message = "Subject is mandatory")
	    private String subject;

	    
	    @NotBlank(message = "Message is mandatory")
	    private String message;

	    

}
