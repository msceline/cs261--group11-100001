package com.team.project.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.dto.auth.UserResponse;
import com.team.project.dto.user.UpdateProfileRequest;
import com.team.project.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestAttribute("userId") int userId) {
        return find(userId);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestAttribute("userId") int userId,
                                      @RequestBody UpdateProfileRequest request) {
        String error = validate(request);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        return userService.updateProfile(userId, request)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(UserResponse.from(u)))
                .orElseGet(UserController::notFound);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getById(@PathVariable int userId) {
        return find(userId);
    }

    private ResponseEntity<?> find(int userId) {
        return userService.findById(userId)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(UserResponse.from(u)))
                .orElseGet(UserController::notFound);
    }

    private static ResponseEntity<?> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
    }

    /** Returns an error message, or null when the request is valid. */
    private static String validate(UpdateProfileRequest req) {
        if (req == null) return "Request body is required";
        if (req.displayName() != null && (req.displayName().isBlank() || req.displayName().length() > 100)) {
            return "displayName must be 1-100 characters";
        }
        if (req.email() != null && (!req.email().contains("@") || req.email().length() > 255)) {
            return "email is invalid";
        }
        return null;
    }
}
