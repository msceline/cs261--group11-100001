package com.team.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
