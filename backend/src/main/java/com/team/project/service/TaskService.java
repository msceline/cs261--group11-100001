
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

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(int taskId) {
        return taskRepository.findById(taskId);
    }

    public Optional<Task> updateTask(int taskId, Task task) {
        return taskRepository.update(taskId, task);
    }
}
