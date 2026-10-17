package com.team.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
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

    @Test
    void getByIdReturnsThatUser() throws Exception {
        when(userService.findById(2)).thenReturn(Optional.of(user(2, "Somying", "y@x.com")));

        mockMvc.perform(get("/api/users/2").requestAttr("userId", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.displayName").value("Somying"));
    }

    @Test
    void getByIdReturns404WhenMissing() throws Exception {
        when(userService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99").requestAttr("userId", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void updateMeReturnsUpdatedProfile() throws Exception {
        when(userService.updateProfile(eq(1), any())).thenReturn(Optional.of(user(1, "New Name", "n@x.com")));

        mockMvc.perform(put("/api/users/me").requestAttr("userId", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"displayName\":\"New Name\",\"email\":\"n@x.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("New Name"));
    }

    @Test
    void updateMeRejectsInvalidInputWithoutCallingService() throws Exception {
        String[] badBodies = {
                "{\"displayName\":\"   \"}",
                "{\"displayName\":\"" + "x".repeat(101) + "\"}",
                "{\"email\":\"not-an-email\"}",
                "{\"email\":\"" + "a".repeat(250) + "@x.com\"}"
        };
        for (String body : badBodies) {
            mockMvc.perform(put("/api/users/me").requestAttr("userId", 1)
                            .contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists());
        }
        verify(userService, never()).updateProfile(any(Integer.class), any());
    }

    @Test
    void updateMeReturns404WhenUserMissing() throws Exception {
        when(userService.updateProfile(eq(9), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/me").requestAttr("userId", 9)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"displayName\":\"Ok\"}"))
                .andExpect(status().isNotFound());
    }
}
