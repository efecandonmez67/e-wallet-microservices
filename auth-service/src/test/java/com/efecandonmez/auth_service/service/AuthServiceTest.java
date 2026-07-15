package com.efecandonmez.auth_service.service;

import com.efecandonmez.auth_service.dto.RegisterRequest;
import com.efecandonmez.auth_service.dto.LoginRequest;
import com.efecandonmez.auth_service.dto.RegisterRequest;
import com.efecandonmez.auth_service.model.User;
import com.efecandonmez.auth_service.repository.UserRepository;
import com.efecandonmez.auth_service.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegister_Successfull() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("efe");
        request.setEmail("efe@gmail.com");
        request.setPassword("12345");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encrypted_12345");

        String result = authService.register(request);

        assertEquals("success: user has been registered!", result);

        verify(passwordEncoder, times(1)).encode("12345");

        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    public void testRegister_UsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("efe");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        String result = authService.register(request);

        assertEquals("Username is already in use", result);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLogin_Successfull() {
        LoginRequest request = new LoginRequest();
        request.setUsername("efe");
        request.setPassword("12345");

        User dummyUser = User.builder()
                .username("efe")
                .password("encrypted_12345")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(dummyUser));

        when(passwordEncoder.matches("12345", "encrypted_12345")).thenReturn(true);

        when(jwtUtil.generateToken("efe")).thenReturn("mock_token_abc");

        String result = authService.login(request);

        assertEquals("mock_token_abc", result);
    }

    @Test
    public void testLogin_PasswordIncorrect() {
        LoginRequest request = new LoginRequest();
        request.setUsername("efe");
        request.setPassword("asdasd_yanlis_sifre");

        User dummyUser = User.builder()
                .username("efe")
                .password("encrypted_12345")
                .build();


        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(dummyUser));

        when(passwordEncoder.matches("asdasd_yanlis_sifre", "encrypted_12345")).thenReturn(false);

        String result = authService.login(request);

        assertEquals("password incorrect", result);

        verify(jwtUtil, never()).generateToken(anyString());

    }


}
