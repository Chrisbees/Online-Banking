package com.chrisbees.bankapp.repo;

import com.chrisbees.bankapp.model.BankAccount;
import com.chrisbees.bankapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByBankAccount(BankAccount account);
}
