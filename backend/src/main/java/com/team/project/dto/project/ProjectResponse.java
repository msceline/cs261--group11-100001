package com.team.project.dto.project;

import java.math.BigDecimal;

import com.team.project.entity.Project;

public record ProjectResponse(int projectId, String projectName, String subjectCode,
                              int hostId, BigDecimal progress) {

    public static ProjectResponse from(Project p) {
        return new ProjectResponse(p.getProjectId(), p.getProjectName(),
                p.getSubjectCode(), p.getHostId(), p.getProgress());
    }
}
