
package com.team.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team.project.entity.Task;
import com.team.project.service.TaskService;
import com.team.project.dto.task.CreateTaskRequest;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Get a task by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Task not found")));
    }

    // Create a new task
    @PostMapping
    public ResponseEntity<Task> createTask(
        @RequestBody CreateTaskRequest request) {

    Task task = new Task(
            0,
            request.getProjectId(),
            request.getTaskTitle(),
            request.getTaskDescription(),
            request.getAssigneeId(),
            request.getStatus(),
            request.getDueDate()
    );

    Task createdTask = taskService.createTask(task);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdTask);
    }
}
