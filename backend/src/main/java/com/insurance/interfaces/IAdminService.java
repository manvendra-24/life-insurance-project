package com.insurance.interfaces;

import com.insurance.response.AdminResponse;
import com.insurance.request.AdminRegisterRequest;
import com.insurance.request.TaxSettingRequest;
import com.insurance.util.PagedResponse;

public interface IAdminService {

	

	



	String createTaxSetting(TaxSettingRequest taxSettingRequest);


	String deleteAdmin(String admin_id);

	String activateAdmin(String admin_id);

	PagedResponse<AdminResponse> getAllAdmins(int page, int size, String sortBy, String direction);

	String updateAdmin(String admin_id, AdminRegisterRequest adminRequest);



	




}
