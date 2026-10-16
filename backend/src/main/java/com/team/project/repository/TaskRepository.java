package com.team.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.team.project.entity.Task;

@Repository
public class TaskRepository {

    private final TaskJpaRepository jpaRepository;

    public TaskRepository(TaskJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public Task save(Task task) {
        Task newTask = new Task(
                null,
                task.getProjectId(),
                task.getTaskTitle(),
                task.getTaskDescription(),
                task.getAssigneeId(),
                task.getStatus(),
                task.getDueDate()
        );

        return jpaRepository.save(newTask);
    }

    public Optional<Task> findById(int taskId) {
        return jpaRepository.findById(taskId);
    }

    public List<Task> findAll() {
        return jpaRepository.findAll();
    }

    public Optional<Task> update(int taskId, Task task) {
        return jpaRepository.findById(taskId)
                .map(existingTask -> {
                    existingTask.setProjectId(task.getProjectId());
                    existingTask.setTaskTitle(task.getTaskTitle());
                    existingTask.setTaskDescription(
                            task.getTaskDescription());
                    existingTask.setAssigneeId(task.getAssigneeId());
                    existingTask.setStatus(task.getStatus());
                    existingTask.setDueDate(task.getDueDate());

                    return jpaRepository.save(existingTask);
                });
    }
}
