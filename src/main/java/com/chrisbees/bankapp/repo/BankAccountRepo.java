package com.chrisbees.bankapp.repo;

import com.chrisbees.bankapp.model.BankAccount;
import com.chrisbees.bankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepo extends JpaRepository<BankAccount, Integer> {

   BankAccount findByUser(User user);

   Optional<BankAccount> findByAccountNumber(String accountNumber);
}
