package com.team.project.dto.user;

/** Both fields are optional; null means "leave unchanged". */
public record UpdateProfileRequest(String displayName, String email) {}
