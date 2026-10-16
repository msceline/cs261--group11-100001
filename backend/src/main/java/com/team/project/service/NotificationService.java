package com.team.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.team.project.entity.Notification;
import com.team.project.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(
            String title,
            String message,
            String type,
            int userId) {

        Notification notification = new Notification(
                null,
                title,
                message,
                false,
                LocalDateTime.now(),
                type,
                userId
        );

        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Optional<Notification> getNotificationById(int id) {
        return notificationRepository.findById(id);
    }
}
