package com.insurance.response;

import com.insurance.entities.InsuranceType;

import lombok.Data;


@Data
public class InsuranceSchemeResponse {

    private String insuranceSchemeId;
    private String name;
    private double newRegistrationCommission;
    
    private double installmentPaymentCommission;
    private String description; 
    private InsuranceType insuranceType;
    private boolean isActive;


}
