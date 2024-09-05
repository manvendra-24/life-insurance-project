package com.insurance.response;


import com.insurance.enums.CreationStatusType;

import lombok.Data;


@Data
public class CustomerResponse {

  private String customerId;
  private String name;
  private String username;
  private String email;
  private boolean isActive;
  private String status;
  
    
}