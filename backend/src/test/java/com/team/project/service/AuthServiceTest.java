package com.team.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.team.project.dto.auth.LoginRequest;
import com.team.project.dto.auth.LoginResponse;
import com.team.project.dto.auth.UserResponse;
import com.team.project.entity.User;
import com.team.project.repository.UserRepository;
import com.team.project.security.JwtService;

class AuthServiceTest {

    /** SHA-256 of "password123". */
    private static final String HASH = "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f";

    private JwtService jwtService;
    private AuthService authService;

    private static User user(int id, String tuId, String role, String password) {
        return new User(id, tuId, role, "User " + id, tuId + "@dome.tu.ac.th", password,
                LocalDateTime.of(2026, 1, 1, 0, 0));
    }

    @BeforeEach
    void setUp() {
        User student = user(1, "6609650001", "student", HASH);
        User student2 = user(2, "6609650002", "student", HASH);
        User noPassword = user(3, "6609650003", "student", null);
        User teacher = user(4, "T00001", "teacher", HASH);

        UserRepository userRepository = mock(UserRepository.class);
        for (User u : new User[] {student, student2, noPassword, teacher}) {
            when(userRepository.findByStudentId(u.getStudentId())).thenReturn(Optional.of(u));
            when(userRepository.findById(u.getUserId())).thenReturn(Optional.of(u));
        }

        jwtService = new JwtService("test-secret-test-secret-test-secret", 3600);
        authService = new AuthService(new UserService(userRepository), jwtService);
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
    void loginForUserWithoutPasswordIsRejected() {
        assertTrue(authService.login(new LoginRequest("6609650003", "password123")).isEmpty());
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

    @Test
    void loginResponseIncludesRole() {
        assertEquals("student", authService.login(new LoginRequest("6609650001", "password123")).orElseThrow().user().role());
        assertEquals("teacher", authService.login(new LoginRequest("T00001", "password123")).orElseThrow().user().role());
    }
}
