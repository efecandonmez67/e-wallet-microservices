package com.efecandonmez.auth_service.service;

import com.efecandonmez.auth_service.dto.RegisterRequest;
import com.efecandonmez.auth_service.model.Role;
import com.efecandonmez.auth_service.model.User;
import com.efecandonmez.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username is already in use";
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email is already in use";
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return "success: user has been registered!";
    }
}
