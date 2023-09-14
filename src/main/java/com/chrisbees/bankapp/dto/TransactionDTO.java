package com.chrisbees.bankapp.dto;

import lombok.Data;

@Data
public class TransactionDTO {

    private String senderAccount;
    private String receiverAccount;
    private String message;
    private double amount;
}
