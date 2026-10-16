package com.team.project.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    private int projectId;
    private String taskTitle;
    private String taskDescription;
    private int assigneeId;
    private String status;
    private LocalDateTime dueDate;

    public Task() {
    }

    public Task(
            Integer taskId,
            int projectId,
            String taskTitle,
            String taskDescription,
            int assigneeId,
            String status,
            LocalDateTime dueDate) {

        this.taskId = taskId;
        this.projectId = projectId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.assigneeId = assigneeId;
        this.status = status;
        this.dueDate = dueDate;
    }

    public Integer getTaskId() {
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
