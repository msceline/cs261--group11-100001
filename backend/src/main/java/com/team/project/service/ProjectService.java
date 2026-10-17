package com.team.project.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team.project.dto.project.CreateProjectRequest;
import com.team.project.dto.project.ProjectResponse;
import com.team.project.entity.Project;
import com.team.project.entity.ProjectMember;
import com.team.project.repository.MemberRepository;
import com.team.project.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public ProjectService(ProjectRepository projectRepository, MemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }

    /** The caller becomes the host and is added as a member with role "host". */
    public ProjectResponse create(int userId, CreateProjectRequest req) {
        if (req == null) throw bad("Request body is required");
        String name = req.projectName() == null ? "" : req.projectName().trim();
        if (name.isEmpty() || name.length() > 150) throw bad("projectName must be 1-150 characters");
        String desc = req.projectDescription() == null ? null : req.projectDescription().trim();
        if (desc != null && desc.length() > 500) throw bad("projectDescription must be at most 500 characters");
        String subject = req.subjectCode() == null ? null : req.subjectCode().trim();
        if (subject != null && subject.length() > 20) throw bad("subjectCode must be at most 20 characters");
        Project project = projectRepository.create(name, desc, subject, userId);
        memberRepository.create(project.getProjectId(), userId, ProjectMember.HOST);
        return ProjectResponse.from(project);
    }

    public List<ProjectResponse> listForMember(int userId) {
        return memberRepository.findByUserId(userId).stream()
                .map(m -> projectRepository.findById(m.getProjectId()))
                .flatMap(java.util.Optional::stream)
                .map(ProjectResponse::from)
                .toList();
    }

    /** Members only. */
    public ProjectResponse get(int userId, int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        if (!memberRepository.existsByProjectIdAndUserId(projectId, userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this project");
        }
        return ProjectResponse.from(project);
    }

    private static ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
