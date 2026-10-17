package com.team.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.team.project.dto.task.CreateTaskRequest;
import com.team.project.entity.Project;
import com.team.project.entity.Task;
import com.team.project.entity.User;
import com.team.project.repository.ProjectRepository;
import com.team.project.repository.TaskRepository;
import com.team.project.repository.UserRepository;

/** Fixture: project 1 and user 2 exist; everything else is unknown. */
class TaskServiceTest {

    private static final LocalDateTime DUE = LocalDateTime.of(2026, 12, 1, 10, 0);

    private TaskRepository taskRepository;
    private TaskService taskService;
    private Project project1;
    private User user2;

    @BeforeEach
    void setUp() {
        User host = new User(1, "6609650001", "student", "Host", "h@x.com", "hash", null);
        user2 = new User(2, "6609650002", "student", "Two", "t@x.com", "hash", null);
        project1 = new Project(1, "P1", "CS261", host, 0, null, null);

        taskRepository = mock(TaskRepository.class);
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        UserRepository userRepository = mock(UserRepository.class);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        taskService = new TaskService(taskRepository, projectRepository, userRepository);
    }

    private static HttpStatus statusOf(ResponseStatusException e) {
        return HttpStatus.valueOf(e.getStatusCode().value());
    }

    private void assertCreateFails(HttpStatus expected, CreateTaskRequest req) {
        assertEquals(expected,
                statusOf(assertThrows(ResponseStatusException.class, () -> taskService.createTask(req))));
        verify(taskRepository, never()).save(any());
    }

    // ---------- create ----------

    @Test
    void createBuildsTaskWithProjectAndAssignee() {
        Task task = taskService.createTask(new CreateTaskRequest(1, "  Write report ", "details", 2, "doing", DUE));

        assertEquals("Write report", task.getTitle());
        assertEquals("details", task.getTaskDescription());
        assertEquals("doing", task.getStatus());
        assertEquals(DUE, task.getDueDate());
        assertEquals(project1, task.getProject());
        assertEquals(user2, task.getAssignee());
    }

    @Test
    void createWithoutAssigneeOrStatusUsesDefaults() {
        Task task = taskService.createTask(new CreateTaskRequest(1, "Task", null, null, null, null));

        assertNull(task.getAssignee());
        assertNull(task.getDueDate());
        assertEquals("todo", task.getStatus());
    }

    @Test
    void createRejectsUnknownProjectAndAssignee() {
        assertCreateFails(HttpStatus.NOT_FOUND, new CreateTaskRequest(999, "Task", null, null, null, null));
        assertCreateFails(HttpStatus.NOT_FOUND, new CreateTaskRequest(1, "Task", null, 999, null, null));
    }

    @Test
    void createRejectsBlankMissingOrTooLongTitle() {
        assertCreateFails(HttpStatus.BAD_REQUEST, new CreateTaskRequest(1, "   ", null, null, null, null));
        assertCreateFails(HttpStatus.BAD_REQUEST, new CreateTaskRequest(1, null, null, null, null, null));
        assertCreateFails(HttpStatus.BAD_REQUEST,
                new CreateTaskRequest(1, "x".repeat(201), null, null, null, null));
    }

    // ---------- read ----------

    @Test
    void getAllAndGetByIdDelegateToRepository() {
        Task task = new Task(5, project1, "T", null, "todo", null, null, null);
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskRepository.findById(5)).thenReturn(Optional.of(task));

        assertEquals(List.of(task), taskService.getAllTasks());
        assertEquals(task, taskService.getTaskById(5).orElseThrow());
        assertTrue(taskService.getTaskById(999).isEmpty());
    }

    // ---------- update ----------

    @Test
    void updateChangesEditableFields() {
        Task existing = new Task(5, project1, "Old", null, "todo", null, null, null);
        when(taskRepository.findById(5)).thenReturn(Optional.of(existing));

        Task updated = taskService.updateTask(5, new CreateTaskRequest(1, "New", "desc", 2, "done", DUE)).orElseThrow();

        assertEquals("New", updated.getTitle());
        assertEquals("desc", updated.getTaskDescription());
        assertEquals("done", updated.getStatus());
        assertEquals(user2, updated.getAssignee());
        assertEquals(DUE, updated.getDueDate());
        assertEquals(project1, updated.getProject());
    }

    @Test
    void updateKeepsStatusWhenNotProvidedAndCanClearAssignee() {
        Task existing = new Task(5, project1, "Old", user2, "doing", DUE, null, null);
        when(taskRepository.findById(5)).thenReturn(Optional.of(existing));

        Task updated = taskService.updateTask(5, new CreateTaskRequest(1, "Old", null, null, null, null)).orElseThrow();

        assertEquals("doing", updated.getStatus());
        assertNull(updated.getAssignee());
        assertNull(updated.getDueDate());
    }

    @Test
    void updateUnknownTaskReturnsEmpty() {
        assertTrue(taskService.updateTask(999, new CreateTaskRequest(1, "T", null, null, null, null)).isEmpty());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateRejectsBlankTitle() {
        Task existing = new Task(5, project1, "Old", null, "todo", null, null, null);
        when(taskRepository.findById(5)).thenReturn(Optional.of(existing));

        assertThrows(ResponseStatusException.class,
                () -> taskService.updateTask(5, new CreateTaskRequest(1, " ", null, null, null, null)));
        verify(taskRepository, never()).save(any());
    }
}
