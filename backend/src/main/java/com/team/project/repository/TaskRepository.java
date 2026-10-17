package com.team.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team.project.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByProject_ProjectId(Integer projectId);

    List<Task> findByAssignee_UserId(Integer assigneeId);

    List<Task> findByProject_ProjectIdAndStatus(Integer projectId, String status);
}

