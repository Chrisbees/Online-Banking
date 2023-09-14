package com.chrisbees.bankapp.services;

import com.chrisbees.bankapp.dto.UserDTO;
import com.chrisbees.bankapp.jwt.JwtService;
import com.chrisbees.bankapp.model.BankAccount;
import com.chrisbees.bankapp.model.Transaction;
import com.chrisbees.bankapp.model.User;
import com.chrisbees.bankapp.repo.BankAccountRepo;
import com.chrisbees.bankapp.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@Configuration
@RequiredArgsConstructor
public class UserService{

    private final UserRepository repository;

    private final JwtService jwtService;

    private final BankAccountRepo bankAccountRepo;


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserDTO createUser(User user){
        var users = repository.findByUsername(user.getUsername());
        if (users.isPresent()){
            throw new RuntimeException("User already exists");
        }
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        String token = jwtService.generateToken(user);
        repository.save(user);
        return new UserDTO(user, token);
    }

    public User getUser(Integer id){
        var user = repository.findById(id);
        if (user.isEmpty()){
            throw new RuntimeException("User does not exists");
        }
        return user.get();
    }


    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public void deleteUser(Integer integer){
        repository.deleteById(integer);
    }

    public User updateUser(Integer id, User user){
       var user1 = getUser(id);
        user1.setUsername(user.getUsername());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setLastName(user.getLastName());
        user1.setFirstName(user.getFirstName());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setRole(user.getRole());

        return repository.save(user1);
    }

    public User createBankAccount(Integer id, BankAccount bankAccount){
        var user = getUser(id);
        // Generate a random 10-digit account number
        String accountNumber = generateRandomAccountNumber();
        var account = bankAccountRepo.findByAccountNumber(accountNumber);
        if (account.isEmpty()){
            bankAccount.setAccountNumber(accountNumber);
            bankAccount.setAccountType(bankAccount.getAccountType());
            bankAccount.setBalance(0.00);
            bankAccount.setActive(true);
            bankAccount.setUser(user);

            List<BankAccount> bankAccounts = new ArrayList<>();
            bankAccounts.add(bankAccount);
            user.setBankAccounts(bankAccounts);
        } else {
            throw new RuntimeException("another user has this account number");
        }
        return repository.save(user);
    }

    // Method to generate a random 10-digit account number
    private String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }

    public List<BankAccount> getUserAccounts (Integer id){
        var user = getUser(id);
        return user.getBankAccounts();
    }

    public BankAccount getUserAccount (Integer userId, Integer accountId ){
        var user = getUser(userId);
        var account = bankAccountRepo.findById(accountId);
        if (account.isEmpty()){
            throw new RuntimeException("Account does not exist");
        } else if (!account.get().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This account does not belong to this user");
        }
        return account.get();
    }

    public void deleteAccount (Integer userId, Integer accountId ){
        var user = getUser(userId);
        var account = bankAccountRepo.findById(accountId);
        if (account.isEmpty()){
            throw new RuntimeException("Account does not exist");
        } else if (!account.get().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This account does not belong to this user");
        }
        bankAccountRepo.deleteById(accountId);
        new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
    }

    public List<Transaction> getTransactionHistory (Integer userId, Integer accountId){
        var account = getUserAccount(userId, accountId);
        return account.getTransactions();
    }

    public void freezeAccount(Integer userId, Integer accountId) {
        var account = getUserAccount(userId, accountId);
            // Implement logic to close or freeze the account here
            account.setActive(false);
        ResponseEntity.ok("Account frozen successfully.");
    }

}

