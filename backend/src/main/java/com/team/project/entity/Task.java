
package com.team.project.entity;

import java.time.LocalDateTime;

public class Task {

    private final int taskId;
    private final int projectId;
    private final String taskTitle;
    private final String taskDescription;
    private final int assigneeId;
    private final String status;
    private final LocalDateTime dueDate;
    private final LocalDateTime startDate;

    public Task(
            int taskId,
            int projectId,
            String taskTitle,
            String taskDescription,
            int assigneeId,
            String status,
            LocalDateTime dueDate,
            LocalDateTime startDate) {

        this.taskId = taskId;
        this.projectId = projectId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assigneeId = assigneeId;
        this.status = status;
        this.dueDate = dueDate;
        this.startDate = startDate;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getAssigneeId() {
        return assigneeId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
}
