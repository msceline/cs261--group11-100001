package com.team.project.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team.project.dto.auth.UserResponse;
import com.team.project.dto.project.AddMemberRequest;
import com.team.project.dto.project.CreateProjectRequest;
import com.team.project.dto.project.ProjectResponse;
import com.team.project.entity.Project;
import com.team.project.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public ProjectResponse create(int userId, CreateProjectRequest req) {
        if (req == null) throw bad("Request body is required");
        String name = req.projectName() == null ? "" : req.projectName().trim();
        if (name.isEmpty() || name.length() > 150) throw bad("projectName must be 1-150 characters");
        String desc = req.projectDescription() == null ? null : req.projectDescription().trim();
        if (desc != null && desc.length() > 500) throw bad("projectDescription must be at most 500 characters");
        String subject = req.subjectCode() == null ? null : req.subjectCode().trim();
        if (subject != null && subject.length() > 20) throw bad("subjectCode must be at most 20 characters");
        return ProjectResponse.from(projectRepository.create(name, desc, subject, userId));
    }

    public List<ProjectResponse> listForMember(int userId) {
        return projectRepository.findByMemberId(userId).stream().map(ProjectResponse::from).toList();
    }

    /** Members only. */
    public ProjectResponse get(int userId, int projectId) {
        return ProjectResponse.from(findAsMember(userId, projectId));
    }

    /** Members only. */
    public List<UserResponse> listMembers(int userId, int projectId) {
        return findAsMember(userId, projectId).getMemberIds().stream()
                .flatMap(id -> userService.findById(id).stream())
                .map(UserResponse::from)
                .toList();
    }

    /** Host only. */
    public UserResponse addMember(int userId, int projectId, AddMemberRequest req) {
        Project project = find(projectId);
        if (project.getHostId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the host can add members");
        }
        if (req == null || req.userId() == null) throw bad("userId is required");
        UserResponse newMember = userService.findById(req.userId())
                .map(UserResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!project.addMember(req.userId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member");
        }
        return newMember;
    }

    private Project find(int projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private Project findAsMember(int userId, int projectId) {
        Project project = find(projectId);
        if (!project.isMember(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this project");
        }
        return project;
    }

    private static ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
