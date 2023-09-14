package com.chrisbees.bankapp.controller;

import com.chrisbees.bankapp.dto.AdminDTO;
import com.chrisbees.bankapp.model.Admin;
import com.chrisbees.bankapp.model.Otp;
import com.chrisbees.bankapp.model.User;
import com.chrisbees.bankapp.services.AdminService;
import com.chrisbees.bankapp.utils.AuthRequest;
import com.chrisbees.bankapp.utils.LoginDTO;
import com.chrisbees.bankapp.utils.LoginRequest;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.twilio.example.Example.ACCOUNT_SID;
import static com.twilio.example.Example.AUTH_TOKEN;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth/v1/admin/")
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final AuthRequest loginRequest;

    @PostMapping("/register")
//    @PreAuthorize("hasRole('ADMIN')")
    public AdminDTO createAdmin(@RequestBody Admin admin){
        return adminService.registerAdmin(admin);
    }

    @PostMapping("/login")
    public LoginDTO login(@RequestBody LoginRequest admin){
        return loginRequest.login(admin);
    }

//    @PostMapping("/verify-otp")
//    public LoginDTO verifyOtp(@RequestBody Otp otp){
//       return loginRequest.verifyOTP(otp.getUsername(), otp.getOtp(), otp.getPassword());
//    }
}

