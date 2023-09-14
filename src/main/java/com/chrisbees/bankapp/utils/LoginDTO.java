package com.chrisbees.bankapp.utils;


import com.chrisbees.bankapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
   private User user;
    private String token;
    private boolean isVerified;
   private String message;
}

