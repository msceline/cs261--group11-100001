package com.team.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.entity.Notification;

public interface NotificationJpaRepository
        extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserId(int userId);
}
