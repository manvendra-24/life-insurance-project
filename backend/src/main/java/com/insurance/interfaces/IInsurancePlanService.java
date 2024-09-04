package com.insurance.interfaces;

import org.springframework.stereotype.Service;

import com.insurance.request.InsurancePlanRequest;
import com.insurance.response.InsurancePlanResponse;
import com.insurance.util.PagedResponse;


@Service
public interface IInsurancePlanService {

	PagedResponse<InsurancePlanResponse> getAllInsurancePlans(int page, int size, String sortBy, String direction);

	String createInsurancePlan(InsurancePlanRequest planRequest);

	String updateInsurancePlan(String id, InsurancePlanRequest planRequest);

	String activateInsurancePlan(String id);

	String deleteInsurancePlan(String id);

}
