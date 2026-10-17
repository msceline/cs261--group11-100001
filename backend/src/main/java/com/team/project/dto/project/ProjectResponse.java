package com.team.project.dto.project;

import com.team.project.entity.Project;

public record ProjectResponse(int projectId, String projectName, String subjectCode,
                              int hostId, int progress) {

    public static ProjectResponse from(Project p) {
        return new ProjectResponse(p.getProjectId(), p.getProjectName(),
                p.getSubjectId(), p.getHost().getUserId(), p.getProgress());
    }
}
