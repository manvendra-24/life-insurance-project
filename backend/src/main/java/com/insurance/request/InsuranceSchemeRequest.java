package com.insurance.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InsuranceSchemeRequest {

    @NotBlank(message = "Scheme name is mandatory")
    private String schemeName;

    @Positive(message = "New registration commission must be positive")
    private double newRegistrationCommission;

    @Positive(message = "Installment payment commission must be positive")
    private double installmentPaymentCommission;

    @NotNull(message = "Insurance Type ID is mandatory")
    private String insuranceTypeId;
}
