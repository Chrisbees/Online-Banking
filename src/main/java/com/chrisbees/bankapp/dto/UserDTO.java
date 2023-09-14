package com.chrisbees.bankapp.dto;

import com.chrisbees.bankapp.model.Admin;
import com.chrisbees.bankapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    public User user;
    public String token;
}
