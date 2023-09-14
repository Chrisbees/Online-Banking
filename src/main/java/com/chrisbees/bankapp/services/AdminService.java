package com.chrisbees.bankapp.services;

import com.chrisbees.bankapp.dto.AdminDTO;
import com.chrisbees.bankapp.jwt.JwtService;
import com.chrisbees.bankapp.model.Admin;
import com.chrisbees.bankapp.model.User;
import com.chrisbees.bankapp.repo.AdminRepository;
import com.chrisbees.bankapp.repo.UserRepository;
import com.chrisbees.bankapp.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public final JwtService tokenService;
    public AdminDTO registerAdmin(Admin admin){
        User user = new User();
//        Authentication auth = new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(admin.getRole());
        user.setFirstName(admin.getFirstname());
        user.setLastName(admin.getLastname());
        user.setEmail(admin.getEmail());
        user.setUsername(admin.getUsername());
        user.setPassword(admin.getPassword());
        user.setPhoneNumber(admin.getPhoneNumber());
        user.setRole(admin.getRole());
        user.setAdmin(admin);
        admin.setUser(user);
        String token = tokenService.generateToken(admin.getUser());
        var admin1 = adminRepository.save(admin);
        return new AdminDTO(admin1, token);

    }

}
