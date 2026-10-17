package com.team.project.entity;

import java.time.LocalDateTime;

public class User {
    private final int userId;
    private final String studentId;
    private final String password;
    private String displayName;
    private String email;
    private final LocalDateTime createdAt;
    private final Role role;

    public User(int userId, String studentId, String password, String displayName, String email, LocalDateTime createdAt,
                Role role) {
        this.userId = userId;
        this.studentId = studentId;
        this.password = password;
        this.displayName = displayName;
        this.email = email;
        this.createdAt = createdAt;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public String getStudentId() { return studentId; }
    public String getPassword() { return password; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Role getRole() { return role; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setEmail(String email) { this.email = email; }
}
