package com.insurance.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StateRequest {

    @NotNull(message = "State ID is mandatory")
    private Long stateId;

    @NotBlank(message = "Name is mandatory")
    private String name;
}
