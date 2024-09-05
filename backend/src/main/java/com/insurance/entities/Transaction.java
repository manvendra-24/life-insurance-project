package com.insurance.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(nullable = false)
    @NotNull(message = "Transaction type cannot be null")
    private String transactionType;  // e.g., PREMIUM_PAYMENT, CLAIM, WITHDRAWAL

    @Column(nullable = false)
    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount must be a positive value")
    private Double amount;

    @Column(nullable = false)
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;

    private String status;  // e.g., SUCCESS, PENDING, FAILED
}
