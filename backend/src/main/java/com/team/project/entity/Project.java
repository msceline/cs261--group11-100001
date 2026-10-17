package com.team.project.entity;

import java.math.BigDecimal;

public class Project {
    private final int projectId;
    private final String projectName;
    private final String subjectCode;
    private final int hostId;
    private BigDecimal progress; // DECIMAL(5,2): 0.00 - 100.00

    public Project(int projectId, String projectName, String subjectCode,
                   int hostId, BigDecimal progress) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.subjectCode = subjectCode;
        this.hostId = hostId;
        this.progress = progress;
    }

    public int getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public String getSubjectCode() { return subjectCode; }
    public int getHostId() { return hostId; }
    public BigDecimal getProgress() { return progress; }

    public void setProgress(BigDecimal progress) { this.progress = progress; }
}
