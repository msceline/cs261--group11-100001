package com.team.project.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.team.project.dto.auth.LoginRequest;
import com.team.project.dto.auth.LoginResponse;
import com.team.project.dto.auth.UserResponse;
import com.team.project.security.JwtService;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /** Returns empty when credentials are invalid. */
    public Optional<LoginResponse> login(LoginRequest req) {
        if (req == null || req.studentId() == null || req.password() == null) return Optional.empty();
        return userService.findByStudentId(req.studentId())
                .filter(u -> u.getPassword() != null && u.getPassword().equals(sha256(req.password())))
                .map(u -> new LoginResponse(jwtService.generateToken(u.getUserId()), UserResponse.from(u)));
    }

    public Optional<UserResponse> me(int userId) {
        return userService.findById(userId).map(UserResponse::from);
    }

    private static String sha256(String s) {
        try {
            return HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
