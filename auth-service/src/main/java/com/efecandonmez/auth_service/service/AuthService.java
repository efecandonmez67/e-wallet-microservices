package com.efecandonmez.auth_service.service;

import com.efecandonmez.auth_service.dto.LoginRequest;
import com.efecandonmez.auth_service.dto.RegisterRequest;
import com.efecandonmez.auth_service.model.Role;
import com.efecandonmez.auth_service.model.User;
import com.efecandonmez.auth_service.repository.UserRepository;
import com.efecandonmez.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

    public String login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if(userOptional.isEmpty()) {
            return "user not found";
        }

        User user = userOptional.get();

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "password incorrect";
        }

        return jwtUtil.generateToken(user.getUsername(), user.getId());
    }
}
