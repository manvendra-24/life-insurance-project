package com.insurance.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(name = "withdrawal_requests")
@Data
public class WithdrawalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalRequestId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @Column(nullable = false)
    @NotBlank(message = "Request type cannot be blank")
    private String requestType;  

    @Column(nullable = false)
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @Column(nullable = false)
    @NotNull(message = "Request date cannot be null")
    private LocalDateTime requestDate;

    @Column(nullable = false)
    @NotBlank(message = "Status cannot be blank")
    private String status;  
    
    
}
