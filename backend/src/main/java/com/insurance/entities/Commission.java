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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "commissions")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commissionId;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    @NotNull(message = "Agent is mandatory")
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    @NotNull(message = "Policy is mandatory")
    private Policy policy;

    @Column(nullable = false)
    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @Column(nullable = false)
    @NotNull(message = "Date is mandatory")
    private LocalDateTime date;

}
