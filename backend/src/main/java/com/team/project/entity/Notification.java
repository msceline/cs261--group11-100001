package com.team.project.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String notificationTitle;
    private String notificationMessage;
    private boolean isRead;
    private LocalDateTime createdAt;
    private String type;
    private int userId;

    // Required by JPA
    public Notification() {
    }

    public Notification(
            Integer id,
            String notificationTitle,
            String notificationMessage,
            boolean isRead,
            LocalDateTime createdAt,
            String type,
            int userId) {
        this.id = id;
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.type = type;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public boolean isRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
