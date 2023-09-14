package com.chrisbees.bankapp.controller;

import com.chrisbees.bankapp.dto.TransactionDTO;
import com.chrisbees.bankapp.dto.UserDTO;
import com.chrisbees.bankapp.model.BankAccount;
import com.chrisbees.bankapp.model.Transaction;
import com.chrisbees.bankapp.model.User;
import com.chrisbees.bankapp.services.TransactionService;
import com.chrisbees.bankapp.services.UserService;
import com.chrisbees.bankapp.utils.AuthRequest;
import com.chrisbees.bankapp.utils.LoginDTO;
import com.chrisbees.bankapp.utils.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    private final TransactionService transactionService;
    private final AuthRequest loginRequest;

    @PostMapping("/register")
    public UserDTO createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("/{userId}/getUser")
    public User getUser(@PathVariable Integer userId){
        return userService.getUser(userId);
    }

    @GetMapping("/allUsers")
    public List<User> allUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/{userId}/delete")
    public void deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}/update")
    public User updateUser(@PathVariable Integer userId, @RequestBody User user){
        return userService.updateUser(userId, user);
    }

    @PostMapping("/login")
    public LoginDTO login(@RequestBody LoginRequest user){
        return loginRequest.login(user);
    }

    @PutMapping("/{userId}/createBankAccount")
    public User createBankAccount(@PathVariable Integer userId, @RequestBody BankAccount bankAccount){
        return userService.createBankAccount(userId, bankAccount);
    }

    @GetMapping("/{userId}/getAccounts")
    public List<BankAccount> getAccounts(@PathVariable Integer userId){
        return userService.getUserAccounts(userId);
    }

    @GetMapping("/{userId}/{accountId}/getAccount")
    public BankAccount getAccount(@PathVariable Integer userId, @PathVariable Integer accountId){
        return userService.getUserAccount(userId, accountId);
    }
    @DeleteMapping("/{userId}/{accountId}/deleteAccount")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer userId, @PathVariable Integer accountId){
        userService.deleteAccount(userId, accountId);
        return new ResponseEntity<>("Account Deleted", HttpStatus.OK);
    }


    @GetMapping("/{userId}/{accountId}/getTransactionHistory")
    public List<Transaction> getAccountTransactions(@PathVariable Integer userId, @PathVariable Integer accountId){
        return userService.getTransactionHistory(userId, accountId);
    }

    @PutMapping("/{userId}/{accountId}/freeze")
    public ResponseEntity<String> freezeAccount(@PathVariable Integer userId, @PathVariable Integer accountId){
         userService.freezeAccount(userId, accountId);
         return ResponseEntity.ok("Successful");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransactionDTO transactionDTO){
        transactionService.performTransaction(transactionDTO);
        return ResponseEntity.ok("Successful");
    }

    @PutMapping("/deposit")
    public ResponseEntity<?> depositMoney(@RequestBody TransactionDTO transactionDTO){
        transactionService.deposit(transactionDTO);
        return new ResponseEntity<>("Successful", HttpStatus.OK);
    }

}
