
package com.team.project.entity;

import java.time.LocalDateTime;

public class Notification {
    private final int id;
    private final String notificationTitle;
    private final String notificationMessage;
    private final boolean isRead;
    private final LocalDateTime createdAt;
    private final String type;
    private final int userId;

    public Notification(
            int id,
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

    public int getId() {
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
}
