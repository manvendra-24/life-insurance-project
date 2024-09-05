package com.insurance.entities;

import java.time.LocalDate;
import java.util.List;

import com.insurance.enums.PaymentInterval;

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
    private String policyId;

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
    
    
    @Column(nullable = false)
    @NotNull(message = "Policy term cannot be null")
    private int policyTerm;
    
    @Column(nullable = false)
    @NotNull(message = "Total Investment Amount cannot be null")
    private int totalInvestmentAmount;
    
    @Column(nullable = false)
    @NotNull(message = "Payment interval cannot be null")
    @Enumerated(EnumType.STRING)
    private PaymentInterval paymentInterval;
    
    @Column(nullable = false)
    @NotNull(message = "Installment Amount cannot be null")
    private int installmentAmount;
    
    
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;
    
    
    @Column(nullable = false)
    @NotNull(message = "Total Amount Paid cannot be null")
    private int totalAmountPaid;
    
    @Column(nullable = false)
    @NotNull(message = "Next Payment Date cannot be null")
    private LocalDate nextPaymentDate;
    
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

}
