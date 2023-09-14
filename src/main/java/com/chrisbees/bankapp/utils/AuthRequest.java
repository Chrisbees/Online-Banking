package com.chrisbees.bankapp.utils;


import com.chrisbees.bankapp.jwt.JwtService;
import com.chrisbees.bankapp.model.Otp;
import com.chrisbees.bankapp.model.User;
import com.chrisbees.bankapp.repo.UserRepository;
import com.chrisbees.bankapp.services.OtpData;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthRequest {
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    public final JwtService tokenService;

    // Store OTPs and their expiration times in memory
    private final Map<String, OtpData> otpDataMap = new ConcurrentHashMap<>();

    public LoginDTO login(LoginRequest user1) {
        var user = userRepository.findByUsername(user1.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user1.getUsername(),
                        user1.getPassword())
        );

        String token = tokenService.generateToken(user);
        return new LoginDTO(user, token, true, "otp verified");


        //CODE TO SEND OTP AS SMS USING TWILIO
//        if (user1.isVerified()) {
//            // If user is already verified, perform normal login
//            return performLogin(user1);
//        } else {
//            // User is not verified, send OTP and wait for verification
//            var user = userRepository.findByUsername(user1.getUsername())
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//            String otp = generateOTP();
//            otpDataMap.put(user.getUsername(), new OtpData(otp, user.getUsername(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));
//
//            // Send OTP via Twilio
//            sendOTPviaTwilio(user.getPhoneNumber(), otp);
//            log.info("OTP sent successfully to " + user.getPhoneNumber());

            // Return a response indicating that OTP verification is required
//            return new LoginDTO(user, null, false, "verify otp"); // The third parameter indicates OTP verification required
//        }
    }

//
//    public LoginDTO verifyOTP(String username, String otp, String password) {
//        OtpData otpData = otpDataMap.get(username);
//
//       if (!otpData.getOtp().equals(otp)){
//            return new LoginDTO(null, null, false, "Invalid Otp");
//        }
//        log.info("Received OTP: " + otp);
//        log.info("Stored OTP: " + otpData.getOtp());
//        // OTP is valid, set isVerified to true and perform login
//        otpDataMap.remove(username); // Remove the OTP from memory
//        LoginRequest user1 = new LoginRequest(username, password, true);
//        return performLogin(user1);
//    }
//
//    private LoginDTO performLogin(LoginRequest user1) {
//        var user = userRepository.findByUsername(user1.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        user1.getUsername(),
//                        user1.getPassword())
//        );
//
//        String token = tokenService.generateToken(user);
//        return new LoginDTO(user, token, true, "otp verified"); // The third parameter indicates successful login
//    }
//
//
//    private String generateOTP() {
//        // Generate a random 6-digit OTP
//        int otp = new Random().nextInt(900000) + 100000;
//        return String.valueOf(otp);
//    }
//
//
//    private void sendOTPviaTwilio(String phoneNumber, String otp) {
//
//        Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));
//
//        // Replace with your Twilio phone number
//
//        Message message = Message.creator(
//                new PhoneNumber(phoneNumber),
//                new PhoneNumber(""),
//                "Your OTP is: " + otp
//        ).create();
//
//        // Check if the message was sent successfully (optional)
//        if (message.getStatus() == Message.Status.SENT) {
//            log.info("OTP sent successfully to " + phoneNumber);
//        } else {
//            log.error("Failed to send OTP: " + message.getErrorMessage());
//        }
//    }

}
