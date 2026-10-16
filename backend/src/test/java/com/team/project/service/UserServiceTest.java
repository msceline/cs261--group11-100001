package com.team.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.team.project.dto.user.UpdateProfileRequest;
import com.team.project.entity.User;
import com.team.project.repository.UserRepository;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new UserRepository());
    }

    @Test
    void findByStudentIdReturnsUser() {
        assertEquals(1, userService.findByStudentId("6609650001").orElseThrow().getUserId());
    }

    @Test
    void findByStudentIdReturnsEmptyWhenMissing() {
        assertTrue(userService.findByStudentId("nope").isEmpty());
    }

    @Test
    void findByIdReturnsUserOrEmpty() {
        assertEquals("6609650002", userService.findById(2).orElseThrow().getStudentId());
        assertTrue(userService.findById(999).isEmpty());
    }

    @Test
    void updateProfileChangesBothFieldsAndTrims() {
        User updated = userService.updateProfile(1, new UpdateProfileRequest("  New Name ", " new@mail.com "))
                .orElseThrow();

        assertEquals("New Name", updated.getDisplayName());
        assertEquals("new@mail.com", updated.getEmail());
        assertEquals("New Name", userService.findById(1).orElseThrow().getDisplayName());
    }

    @Test
    void updateProfileKeepsFieldsThatAreNull() {
        String originalEmail = userService.findById(1).orElseThrow().getEmail();

        User updated = userService.updateProfile(1, new UpdateProfileRequest("Only Name", null)).orElseThrow();

        assertEquals("Only Name", updated.getDisplayName());
        assertEquals(originalEmail, updated.getEmail());
    }

    @Test
    void updateProfileReturnsEmptyForUnknownUser() {
        assertTrue(userService.updateProfile(999, new UpdateProfileRequest("x", "x@x")).isEmpty());
    }
}
