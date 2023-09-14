package com.chrisbees.bankapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private double amount;
    private String transactionType;
    private String sender;
    private String receiver;
    private LocalDateTime transactionDateTime;

    // Many-to-one relationship with Account for associating transactions with accounts
    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    // Getters and setters
}