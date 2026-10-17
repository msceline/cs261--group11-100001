package com.team.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.dto.notification.CreateNotificationRequest;
import com.team.project.dto.notification.NotificationResponse;
import com.team.project.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public List<NotificationResponse> getNotificationsByUserId(@PathVariable int userId) {
        return notificationService.getNotificationsByUserId(userId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable int id) {
        return notificationService.getNotificationById(id)
                .map(n -> ResponseEntity.ok(NotificationResponse.from(n)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody CreateNotificationRequest request) {
        var notification = notificationService.createNotification(
                request.getNotificationTitle(),
                request.getNotificationMessage(),
                request.getType(),
                request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(NotificationResponse.from(notification));
    }
}
