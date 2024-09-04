package com.insurance.response;

import java.util.List;
import lombok.Data;

@Data
public class StateResponse {
   
    private String stateId;
    private String name;
    private boolean isActive;
    private List<CityResponse> cities; 
}
