
package com.team.project.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.team.project.entity.Task;

@Repository
public class TaskRepository {

    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    // Add a new task

    public Task save(Task task) {
        Task newTask = new Task(
            nextId++,
            task.getProjectId(),
            task.getTaskTitle(),
            task.getTaskDescription(),
            task.getAssigneeId(),
            task.getStatus(),
            task.getDueDate()
    );

    tasks.add(newTask);
    return newTask;
}


    // Find a task by ID
    public Optional<Task> findById(int taskId) {
        return tasks.stream()
                .filter(task -> task.getTaskId() == taskId)
                .findFirst();
    }

    // Get all tasks
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }
}
