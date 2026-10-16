package com.team.project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.entity.Notification;
import com.team.project.service.NotificationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.team.project.dto.notification.CreateNotificationRequest;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Get notifications for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(
            @PathVariable int userId) {

        List<Notification> notifications =
                notificationService.getNotificationsByUserId(userId);

        return ResponseEntity.ok(notifications);
    }

    // Get a notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(
            @PathVariable int id) {

        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    @PostMapping
    public ResponseEntity<Notification> createNotification(
        @RequestBody CreateNotificationRequest request) {

        Notification notification = notificationService.createNotification(
            request.getNotificationTitle(),
            request.getNotificationMessage(),
            request.getType(),
            request.getUserId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

}
