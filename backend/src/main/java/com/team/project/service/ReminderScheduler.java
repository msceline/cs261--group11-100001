package com.team.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.team.project.entity.Task;

@Component
public class ReminderScheduler {

        private static final ZoneId ZONE = ZoneId.of("Asia/Bangkok");

        private final TaskService taskService;
        private final NotificationService notificationService;

        private final Set<String> sentEvents = new HashSet<>();

        private LocalDateTime lastCheckAt;

        public ReminderScheduler(
                        TaskService taskService,
                        NotificationService notificationService) {

                this.taskService = taskService;
                this.notificationService = notificationService;
        }

        @Scheduled(fixedRate = 60000)
        public void checkReminders() {

                LocalDateTime now = LocalDateTime.now(ZONE);

                LocalDateTime previousCheck = (lastCheckAt == null)
                                ? now.minusMinutes(1)
                                : lastCheckAt;

                lastCheckAt = now;

                System.out.println(
                                "ReminderScheduler is running: " + now);

                for (Task task : taskService.getAllTasks()) {

                        System.out.println(
                                        "Checking task: " + task.getTaskId()
                                                        + ", dueDate: " + task.getDueDate()
                                                        + ", status: " + task.getStatus());

                        if (task.getDueDate() == null) {
                                continue;
                        }

                        String status = task.getStatus();

                        if (status != null
                                        && status.equalsIgnoreCase("COMPLETED")) {
                                continue;
                        }

                        LocalDateTime dueDate = task.getDueDate();

                        if (!now.isBefore(dueDate)) {
                                continue;
                        }

                        LocalDate today = now.toLocalDate();

                        LocalDateTime dailyTime = LocalDateTime.of(
                                        today, LocalTime.of(9, 0));

                        if (dueDate.isAfter(dailyTime)
                                        && !dailyTime.isBefore(previousCheck)
                                        && !dailyTime.isAfter(now)) {
                                sendOnce(
                                                task,
                                                "DAILY",
                                                dailyTime,
                                                "Daily task reminder",
                                                "Task \"" + task.getTaskTitle()
                                                                + "\" is due on " + dueDate);
                        }

                        sendDeadlineReminder(
                                        task, dueDate, now, previousCheck,
                                        60, "1_HOUR",
                                        "Task deadline in 1 hour");

                        sendDeadlineReminder(
                                        task, dueDate, now, previousCheck,
                                        30, "30_MINUTES",
                                        "Task deadline in 30 minutes");

                        sendDeadlineReminder(
                                        task, dueDate, now, previousCheck,
                                        10, "10_MINUTES",
                                        "Task deadline in 10 minutes");

                }
        }

        private void sendDeadlineReminder(
                        Task task,
                        LocalDateTime dueDate,
                        LocalDateTime now,
                        LocalDateTime previousCheck,
                        int minutesBefore,
                        String eventType,
                        String title) {

                LocalDateTime reminderTime = dueDate.minusMinutes(minutesBefore);

                if (!reminderTime.isBefore(previousCheck)
                                && !reminderTime.isAfter(now)) {
                        sendOnce(
                                        task,
                                        eventType,
                                        reminderTime,
                                        title,
                                        "Task \"" + task.getTaskTitle()
                                                        + "\" is due on " + dueDate);
                }
        }

        private void sendOnce(
                        Task task,
                        String eventType,
                        LocalDateTime eventTime,
                        String title,
                        String message) {

                String eventKey = task.getTaskId()
                                + "_" + eventType
                                + "_" + eventTime;

                if (sentEvents.contains(eventKey)) {
                        return;
                }

                notificationService.createNotification(
                                title,
                                message,
                                "TASK_REMINDER",
                                task.getAssigneeId());

                sentEvents.add(eventKey);

                System.out.println(
                                "Notification created for task "
                                                + task.getTaskId()
                                                + ", type: " + eventType
                                                + ", userId: " + task.getAssigneeId());
        }
}
