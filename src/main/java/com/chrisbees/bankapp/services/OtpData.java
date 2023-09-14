package com.chrisbees.bankapp.services;

import lombok.Data;

@Data
public class OtpData {

        private final String otp;
        private final String username;
        private final long expirationTime;
        public OtpData(String otp, String username, long expirationTime) {
            this.otp = otp;
            this.username = username;
            this.expirationTime = expirationTime;
        }

    }
