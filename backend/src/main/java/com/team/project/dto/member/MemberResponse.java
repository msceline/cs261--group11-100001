package com.team.project.dto.member;

import com.team.project.entity.ProjectMember;
import com.team.project.entity.User;

public record MemberResponse(int id, int projectId, int userId, String roleInProject,
                             String studentId, String displayName) {

    public static MemberResponse from(ProjectMember m, User u) {
        return new MemberResponse(m.getId(), m.getProjectId(), m.getUserId(), m.getRoleInProject(),
                u.getStudentId(), u.getDisplayName());
    }
}
