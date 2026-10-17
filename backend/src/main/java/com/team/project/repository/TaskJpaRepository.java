package com.team.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.team.project.entity.Task;

public interface TaskJpaRepository
                extends JpaRepository<Task, Integer> {
}
