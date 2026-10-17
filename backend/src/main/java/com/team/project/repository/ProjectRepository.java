package com.team.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team.project.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByHost_UserId(Integer hostId);

    List<Project> findBySubjectId(String subjectId);

    List<Project> findByProjectNameContainingIgnoreCase(String keyword);
}

