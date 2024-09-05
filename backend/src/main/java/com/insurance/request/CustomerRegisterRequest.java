package com.insurance.request;

import java.time.LocalDate;
import java.util.List;

import com.insurance.entities.Agent;
import com.insurance.entities.City;
import com.insurance.entities.Document;
import com.insurance.entities.Employee;
import com.insurance.entities.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CustomerRegisterRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Date of birth is mandatory")
    private String dob;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "Phone number is mandatory")
    @Size(min = 10, message = "Phone number must be at least 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Bank account details are mandatory")
    private String bankAccountDetails;

    @NotNull(message = "City is mandatory")
    private String cityId;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

//    @NotNull(message = "Employee ID is mandatory")
//    private String employeeId;
}
