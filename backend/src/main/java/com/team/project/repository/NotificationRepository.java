package com.team.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.team.project.entity.Notification;

@Repository
public class NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    public NotificationRepository(NotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // Find notifications by user ID
    public List<Notification> findByUserId(int userId) {
        return jpaRepository.findByUserId(userId);
    }

    // Save a notification
    public Notification save(Notification notification) {
        return jpaRepository.save(notification);
    }

    // Find a notification by ID
    public Optional<Notification> findById(int id) {
        return jpaRepository.findById(id);
    }

    // Get all notifications
    public List<Notification> findAll() {
        return jpaRepository.findAll();
    }
}
