
package com.team.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.team.project.entity.Task;
import com.team.project.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create a new task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get a task by ID
    public Optional<Task> getTaskById(int taskId) {
        return taskRepository.findById(taskId);
    }
}
