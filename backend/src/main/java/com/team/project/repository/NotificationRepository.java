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

    public List<Notification> findByUserId(int userId) {
        return jpaRepository.findByUserId(userId);
    }

    public Notification save(Notification notification) {
        return jpaRepository.save(notification);
    }

    public Optional<Notification> findById(int id) {
        return jpaRepository.findById(id);
    }

    public List<Notification> findAll() {
        return jpaRepository.findAll();
    }
}
