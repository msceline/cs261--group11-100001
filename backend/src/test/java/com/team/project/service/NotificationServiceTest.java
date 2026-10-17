package com.team.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.team.project.entity.Notification;
import com.team.project.entity.User;
import com.team.project.repository.NotificationRepository;
import com.team.project.repository.UserRepository;

class NotificationServiceTest {

    private NotificationRepository notificationRepository;
    private NotificationService notificationService;
    private User user2;

    @BeforeEach
    void setUp() {
        user2 = new User(2, "6609650002", "student", "Two", "t@x.com", "hash", null);
        notificationRepository = mock(NotificationRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getReferenceById(2)).thenReturn(user2);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        notificationService = new NotificationService(notificationRepository, userRepository);
    }

    @Test
    void createSavesUnreadNotificationForUser() {
        Notification created = notificationService.createNotification("Title", "Message", "invite", 2);

        ArgumentCaptor<Notification> saved = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(saved.capture());
        assertEquals(created, saved.getValue());
        assertEquals("Title", created.getTitle());
        assertEquals("Message", created.getMessage());
        assertEquals("invite", created.getType());
        assertEquals(user2, created.getUser());
        assertFalse(created.getIsRead());
    }

    @Test
    void getByUserIdReturnsNewestFirstFromRepository() {
        Notification newer = new Notification(2, user2, "New", "m", "t", false, LocalDateTime.of(2026, 2, 1, 0, 0));
        Notification older = new Notification(1, user2, "Old", "m", "t", true, LocalDateTime.of(2026, 1, 1, 0, 0));
        when(notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(2)).thenReturn(List.of(newer, older));

        assertEquals(List.of(newer, older), notificationService.getNotificationsByUserId(2));
        assertTrue(notificationService.getNotificationsByUserId(3).isEmpty());
    }

    @Test
    void getByIdReturnsNotificationOrEmpty() {
        Notification n = new Notification(1, user2, "T", "m", "t", false, null);
        when(notificationRepository.findById(1)).thenReturn(Optional.of(n));

        assertEquals(n, notificationService.getNotificationById(1).orElseThrow());
        assertTrue(notificationService.getNotificationById(999).isEmpty());
    }
}
