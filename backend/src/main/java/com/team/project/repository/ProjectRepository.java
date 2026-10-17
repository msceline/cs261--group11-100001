package com.team.project.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import com.team.project.entity.Project;

/** Mock repository backed by in-memory data (not connected to SQL yet). */
@Repository
public class ProjectRepository {

    private final List<Project> projects = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public ProjectRepository() {
        projects.add(new Project(nextId.getAndIncrement(), "Library Management System",
                "Group project for CS261", "CS261", 1, new BigDecimal("35.50")));
        projects.add(new Project(nextId.getAndIncrement(), "Mobile App Prototype",
                "Prototype for the UX course", "CS251", 2, new BigDecimal("0.00")));
    }

    public Optional<Project> findById(int projectId) {
        return projects.stream().filter(p -> p.getProjectId() == projectId).findFirst();
    }

    /** Creates a new project (id assigned here, like an IDENTITY column). */
    public Project create(String name, String description, String subjectCode, int hostId) {
        Project project = new Project(nextId.getAndIncrement(), name, description, subjectCode, hostId,
                new BigDecimal("0.00"));
        projects.add(project);
        return project;
    }
}
