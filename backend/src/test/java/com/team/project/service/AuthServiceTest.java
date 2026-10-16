package com.team.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.team.project.dto.auth.LoginRequest;
import com.team.project.dto.auth.LoginResponse;
import com.team.project.dto.auth.UserResponse;
import com.team.project.repository.UserRepository;
import com.team.project.security.JwtService;

class AuthServiceTest {

    private JwtService jwtService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("test-secret-test-secret-test-secret", 3600);
        authService = new AuthService(new UserService(new UserRepository()), jwtService);
    }

    @Test
    void loginWithValidCredentialsReturnsTokenAndUser() {
        Optional<LoginResponse> result = authService.login(new LoginRequest("6609650001", "password123"));

        assertTrue(result.isPresent());
        assertEquals("6609650001", result.get().user().studentId());
        assertEquals(1, result.get().user().userId());
        assertEquals(1, jwtService.validateAndGetUserId(result.get().token()).getAsInt());
    }

    @Test
    void loginResponseDoesNotLeakPassword() {
        LoginResponse response = authService.login(new LoginRequest("6609650001", "password123")).orElseThrow();

        assertFalse(response.user().toString().contains("password"));
    }

    @Test
    void loginWithWrongPasswordIsRejected() {
        assertTrue(authService.login(new LoginRequest("6609650001", "wrong")).isEmpty());
    }

    @Test
    void loginWithUnknownStudentIdIsRejected() {
        assertTrue(authService.login(new LoginRequest("0000000000", "password123")).isEmpty());
    }

    @Test
    void loginWithMissingFieldsIsRejected() {
        assertTrue(authService.login(null).isEmpty());
        assertTrue(authService.login(new LoginRequest(null, "password123")).isEmpty());
        assertTrue(authService.login(new LoginRequest("6609650001", null)).isEmpty());
    }

    @Test
    void meReturnsExistingUser() {
        Optional<UserResponse> me = authService.me(2);

        assertTrue(me.isPresent());
        assertEquals("6609650002", me.get().studentId());
    }

    @Test
    void meReturnsEmptyForUnknownUser() {
        assertTrue(authService.me(999).isEmpty());
    }
}
