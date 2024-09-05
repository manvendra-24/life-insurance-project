package com.insurance.response;

import java.time.LocalDate;

import com.insurance.enums.PaymentInterval;


import lombok.Data;

@Data
public class PolicyResponse {
	
    private String policyId;
    
    private String plan_id;

    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private int policyTerm;
    
    private int totalInvestmentAmount;
   
    private PaymentInterval paymentInterval;
    
    private int installmentAmount;
    
    private int totalAmountPaid;
    
    private LocalDate nextPaymentDate;
	
}
