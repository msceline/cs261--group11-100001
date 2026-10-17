package com.team.project.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team.project.dto.member.AddMemberRequest;
import com.team.project.dto.member.MemberResponse;
import com.team.project.entity.Project;
import com.team.project.entity.ProjectMember;
import com.team.project.entity.User;
import com.team.project.repository.MemberRepository;
import com.team.project.repository.ProjectRepository;

@Service
public class MemberService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final UserService userService;

    public MemberService(ProjectRepository projectRepository, MemberRepository memberRepository,
                         UserService userService) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.userService = userService;
    }

    /** Members only. */
    public List<MemberResponse> list(int userId, int projectId) {
        findProject(projectId);
        if (!memberRepository.existsByProjectIdAndUserId(projectId, userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this project");
        }
        return memberRepository.findByProjectId(projectId).stream()
                .flatMap(m -> userService.findById(m.getUserId()).map(u -> MemberResponse.from(m, u)).stream())
                .toList();
    }

    /** Host only. The host role itself comes from the project's host_id, so only member/visitor can be assigned. */
    public MemberResponse add(int userId, int projectId, AddMemberRequest req) {
        Project project = findProject(projectId);
        if (project.getHostId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the host can add members");
        }
        if (req == null || req.userId() == null) throw bad("userId is required");
        String role = req.roleInProject() == null ? ProjectMember.MEMBER : req.roleInProject().trim().toLowerCase();
        if (!role.equals(ProjectMember.MEMBER) && !role.equals(ProjectMember.VISITOR)) {
            throw bad("roleInProject must be 'member' or 'visitor'");
        }
        User user = userService.findById(req.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (memberRepository.existsByProjectIdAndUserId(projectId, user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member");
        }
        return MemberResponse.from(memberRepository.create(projectId, user.getUserId(), role), user);
    }

    private Project findProject(int projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private static ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
