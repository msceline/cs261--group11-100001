package com.team.project.dto.auth;

import java.time.LocalDateTime;

import com.team.project.entity.User;

public record UserResponse(int userId, String studentId, String displayName, String email, LocalDateTime createdAt) {

    public static UserResponse from(User u) {
        return new UserResponse(u.getUserId(), u.getStudentId(), u.getDisplayName(), u.getEmail(), u.getCreatedAt());
    }
}
