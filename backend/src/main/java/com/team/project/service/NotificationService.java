package com.team.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.team.project.entity.Notification;
import com.team.project.repository.NotificationRepository;
import com.team.project.repository.UserRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public Notification createNotification(String title, String message, String type, int userId) {
        Notification notification = new Notification(
                null, userRepository.getReferenceById(userId), title, message, type, false, null);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<Notification> getNotificationById(int id) {
        return notificationRepository.findById(id);
    }
}
