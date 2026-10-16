package com.team.project.entity;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Project {
    private final int projectId;
    private String projectName;
    private String projectDescription;
    private final String subjectCode;
    private final int hostId;
    private BigDecimal progress; // DECIMAL(5,2): 0.00 - 100.00
    // Mock membership; the host is always a member. Will become a separate table once SQL is connected.
    private final Set<Integer> memberIds = ConcurrentHashMap.newKeySet();

    public Project(int projectId, String projectName, String projectDescription, String subjectCode,
                   int hostId, BigDecimal progress) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.subjectCode = subjectCode;
        this.hostId = hostId;
        this.progress = progress;
        this.memberIds.add(hostId);
    }

    public int getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public String getProjectDescription() { return projectDescription; }
    public String getSubjectCode() { return subjectCode; }
    public int getHostId() { return hostId; }
    public BigDecimal getProgress() { return progress; }

    public void setProgress(BigDecimal progress) { this.progress = progress; }

    public void addMember(int userId) { memberIds.add(userId); }
    public boolean isMember(int userId) { return memberIds.contains(userId); }
}
