package com.team.project.entity;

import java.util.Set;

public class ProjectMember {

    public static final String HOST = "host";
    public static final String MEMBER = "member";
    public static final String VISITOR = "visitor";
    public static final Set<String> ROLES = Set.of(HOST, MEMBER, VISITOR);

    private final int id;
    private final int projectId;
    private final int userId;
    private final String roleInProject;

    public ProjectMember(int id, int projectId, int userId, String roleInProject) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.roleInProject = roleInProject;
    }

    public int getId() { return id; }
    public int getProjectId() { return projectId; }
    public int getUserId() { return userId; }
    public String getRoleInProject() { return roleInProject; }
}
