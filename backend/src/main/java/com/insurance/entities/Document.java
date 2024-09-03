package com.insurance.entities;

import java.time.LocalDate;

import com.insurance.enums.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Document type is mandatory")
    private DocumentType documentType;

    @Column(nullable = false)
    @NotBlank(message = "Document name is mandatory")
    private String documentName;

    @Column(nullable = false)
    @NotBlank(message = "Document path is mandatory")
    private String documentPath; 

    @Column(nullable = false)
    @NotNull(message = "Upload date is mandatory")
    private LocalDate uploadDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = true)
    private Agent agent;

    
}
