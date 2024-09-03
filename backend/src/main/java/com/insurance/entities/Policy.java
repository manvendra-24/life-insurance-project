package com.insurance.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Entity
@Table(name = "policies")
@Data
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyId;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    @NotNull(message = "Insurance plan cannot be null")
    private InsurancePlan plan;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer cannot be null")
    private Customer customer;

    @Column(nullable = false)
    @NotNull(message = "Start date cannot be null")
    @PastOrPresent(message = "Start date must be in the past or present")
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be in the future or present")
    private LocalDate endDate;

    private String status; 

    private LocalDate nextPaymentDate;

}
