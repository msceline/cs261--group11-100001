package com.team.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team.project.dto.task.CreateTaskRequest;
import com.team.project.entity.Task;
import com.team.project.repository.ProjectRepository;
import com.team.project.repository.TaskRepository;
import com.team.project.repository.UserRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(CreateTaskRequest req) {
        Task task = new Task();
        task.setProject(projectRepository.findById(req.projectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found")));
        apply(task, req);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(int taskId) {
        return taskRepository.findById(taskId);
    }

    /** Updates the editable fields of an existing task; empty when the task does not exist. */
    public Optional<Task> updateTask(int taskId, CreateTaskRequest req) {
        return taskRepository.findById(taskId).map(task -> {
            apply(task, req);
            return taskRepository.save(task);
        });
    }

    private void apply(Task task, CreateTaskRequest req) {
        String title = req.taskTitle() == null ? "" : req.taskTitle().trim();
        if (title.isEmpty() || title.length() > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "taskTitle must be 1-200 characters");
        }
        task.setTitle(title);
        task.setTaskDescription(req.taskDescription());
        task.setDueDate(req.dueDate());
        if (req.status() != null) {
            task.setStatus(req.status());
        }
        task.setAssignee(req.assigneeId() == null ? null : userRepository.findById(req.assigneeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignee not found")));
    }
}
