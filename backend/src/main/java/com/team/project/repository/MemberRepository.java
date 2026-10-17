package com.team.project.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import com.team.project.entity.ProjectMember;

/** Mock repository backed by in-memory data (not connected to SQL yet). */
@Repository
public class MemberRepository {

    private final List<ProjectMember> members = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public MemberRepository() {
        // matches the projects seeded in ProjectRepository
        create(1, 1, ProjectMember.HOST);
        create(1, 2, ProjectMember.MEMBER);
        create(2, 2, ProjectMember.HOST);
    }

    public List<ProjectMember> findByProjectId(int projectId) {
        return members.stream().filter(m -> m.getProjectId() == projectId).toList();
    }

    public List<ProjectMember> findByUserId(int userId) {
        return members.stream().filter(m -> m.getUserId() == userId).toList();
    }

    public Optional<ProjectMember> findByProjectIdAndUserId(int projectId, int userId) {
        return members.stream()
                .filter(m -> m.getProjectId() == projectId && m.getUserId() == userId)
                .findFirst();
    }

    public boolean existsByProjectIdAndUserId(int projectId, int userId) {
        return findByProjectIdAndUserId(projectId, userId).isPresent();
    }

    public ProjectMember create(int projectId, int userId, String roleInProject) {
        ProjectMember member = new ProjectMember(nextId.getAndIncrement(), projectId, userId, roleInProject);
        members.add(member);
        return member;
    }
}
