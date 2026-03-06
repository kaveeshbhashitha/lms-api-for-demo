package com.lms.service;

import java.time.Instant;
import java.time.Year;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.dto.auth.AuthResponse;
import com.lms.dto.auth.LoginRequest;
import com.lms.dto.auth.RegisterRequest;
import com.lms.exception.BadRequestException;
import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import com.lms.security.JwtTokenProvider;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already in use");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        user.setEnrolledYear(Year.now().getValue());
        user.setRole(Role.STUDENT);
        User savedUser = userRepository.save(user);
        log.info("User registered: {}", savedUser.getEmail());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password());
        var authentication = authenticationManager.authenticate(authToken);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return new AuthResponse(jwt, savedUser.getRole(), savedUser.getId());
    }

    public AuthResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));
        return new AuthResponse(token, user.getRole(), user.getId());
    }
}
