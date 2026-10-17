package com.team.project.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.team.project.entity.Role;
import com.team.project.entity.User;
import com.team.project.service.UserService;

class UserControllerTest {

    private UserService userService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    private static User user(int id, String name, String email) {
        return new User(id, "660965000" + id, "hash", name, email, LocalDateTime.of(2026, 1, 1, 0, 0), Role.STUDENT);
    }

    @Test
    void getMeReturnsOwnProfileWithoutPassword() throws Exception {
        when(userService.findById(1)).thenReturn(Optional.of(user(1, "Somchai", "s@x.com")));

        mockMvc.perform(get("/api/users/me").requestAttr("userId", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("s@x.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getMeReturns404WhenMissing() throws Exception {
        when(userService.findById(9)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/me").requestAttr("userId", 9))
                .andExpect(status().isNotFound());
    }
}
