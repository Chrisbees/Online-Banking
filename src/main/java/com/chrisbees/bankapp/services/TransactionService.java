package com.chrisbees.bankapp.services;


import com.chrisbees.bankapp.dto.TransactionDTO;
import com.chrisbees.bankapp.model.Transaction;
import com.chrisbees.bankapp.repo.BankAccountRepo;
import com.chrisbees.bankapp.repo.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BankAccountRepo bankAccountRepo;
    private final TransactionRepository transactionRepository;



    public ResponseEntity<?> performTransaction(TransactionDTO transactionDTO){
        var sender = bankAccountRepo.findByAccountNumber(transactionDTO.getSenderAccount());
        var receiver = bankAccountRepo.findByAccountNumber(transactionDTO.getReceiverAccount());
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        if (sender.isPresent() && receiver.isPresent()){
            var send = sender.get();
            var receive = receiver.get();
            if (send.getBalance() < transactionDTO.getAmount()){
                throw new RuntimeException("Insufficient Funds");
            }
            transaction1.setAmount(transactionDTO.getAmount());
            transaction1.setDescription(transactionDTO.getMessage());
            transaction1.setTransactionDateTime(LocalDateTime.now());
            transaction1.setReceiver(receive.getUser().getFirstName() + " " + receive.getAccountNumber());
            transaction1.setTransactionType("Debit");
            transaction1.setBankAccount(send);

            transaction2.setAmount(transactionDTO.getAmount());
            transaction2.setDescription(transactionDTO.getMessage());
            transaction2.setTransactionDateTime(LocalDateTime.now());
            transaction2.setSender(send.getUser().getFirstName());
            transaction2.setTransactionType("Debit");
            transaction2.setBankAccount(receive);

            List<Transaction> transactionList = new ArrayList<>();
            transactionList.add(transaction1);
            transactionList.add(transaction2);
            send.setBalance(send.getBalance() - transactionDTO.getAmount());
            receive.setBalance(receive.getBalance() + transactionDTO.getAmount());
            send.setTransactions(transactionList);
            bankAccountRepo.save(send);
            bankAccountRepo.save(receive);
            return ResponseEntity.ok("Transfer successful");
        }

       throw  new RuntimeException("Transfer Unsuccessful");
    }

        public ResponseEntity<?> deposit(TransactionDTO transactionDTO){
        var receiverAccount = bankAccountRepo.findByAccountNumber(transactionDTO.getReceiverAccount());
        Transaction transaction = new Transaction();
        if (receiverAccount.isPresent()){
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDescription(transactionDTO.getMessage());
            transaction.setTransactionDateTime(LocalDateTime.now());
            transaction.setTransactionType("Deposit");
            transaction.setBankAccount(receiverAccount.get());
            List<Transaction> transactionList = new ArrayList<>();
            transactionList.add(transaction);
            var receiver = receiverAccount.get();
            receiver.setBalance(receiver.getBalance() + transactionDTO.getAmount());
            receiver.setTransactions(transactionList);
            bankAccountRepo.save(receiver);
            return ResponseEntity.ok("Deposit of " + transactionDTO.getAmount() + " is successful");
        } else {
            throw  new RuntimeException("Account does not exist");
        }
        }
}
