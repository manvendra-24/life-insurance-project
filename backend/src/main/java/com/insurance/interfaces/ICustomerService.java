package com.insurance.interfaces;

import com.insurance.request.CustomerRegisterRequest;
import com.insurance.response.CustomerResponse;
import com.insurance.util.PagedResponse;

import jakarta.validation.Valid;

public interface ICustomerService {

	
	String registerCustomer(String token, @Valid CustomerRegisterRequest registerDto) ;

	PagedResponse<CustomerResponse> getAllCustomers(int page, int size, String sortBy, String direction);

	String updateCustomer(String id, @Valid CustomerRegisterRequest registerDto);

	String deactivateCustomer(String id);

	String activateCustomer(String id);
	
	String verifyCustomerapprove(String token, String id);

	String verifyCustomerReject(String token, String id);

}
