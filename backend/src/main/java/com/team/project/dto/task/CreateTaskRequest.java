
package com.team.project.dto.task;

import java.time.LocalDateTime;

public class CreateTaskRequest {

    private int projectId;
    private String taskTitle;
    private String taskDescription;
    private int assigneeId;
    private String status;
    private LocalDateTime dueDate;

    // Constructor เปล่า ให้ Jackson สร้าง object ได้
    public CreateTaskRequest() {
    }

    // Getters
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

    // Setters
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setAssigneeId(int assigneeId) {
        this.assigneeId = assigneeId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
