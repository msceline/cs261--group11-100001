package com.team.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.team.project.dto.task.CreateTaskRequest;
import com.team.project.entity.Project;
import com.team.project.entity.Task;
import com.team.project.entity.User;
import com.team.project.service.TaskService;

class TaskControllerTest {

    private static final String BODY = "{\"projectId\":1,\"taskTitle\":\"Report\",\"taskDescription\":\"d\","
            + "\"assigneeId\":2,\"status\":\"doing\",\"dueDate\":\"2026-12-01T10:00:00\"}";

    private TaskService taskService;
    private MockMvc mockMvc;
    private Project project;
    private User assignee;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService)).build();
        User host = new User(1, "6609650001", "student", "Host", "h@x.com", "hash", null);
        assignee = new User(2, "6609650002", "student", "Two", "t@x.com", "hash", null);
        project = new Project(1, "P1", "CS261", host, 0, null, null);
    }

    private Task task(int id, String title) {
        return new Task(id, project, title, assignee, "doing", LocalDateTime.of(2026, 12, 1, 10, 0), "d", null);
    }

    @Test
    void getAllReturnsTaskResponses() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(task(1, "A"), task(2, "B")));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].taskId").value(1))
                .andExpect(jsonPath("$[0].projectId").value(1))
                .andExpect(jsonPath("$[0].taskTitle").value("A"))
                .andExpect(jsonPath("$[0].assigneeId").value(2))
                .andExpect(jsonPath("$[1].taskTitle").value("B"));
    }

    @Test
    void getByIdReturnsTask() throws Exception {
        when(taskService.getTaskById(1)).thenReturn(Optional.of(task(1, "A")));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(1))
                .andExpect(jsonPath("$.status").value("doing"))
                .andExpect(jsonPath("$.dueDate").exists());
    }

    @Test
    void getByIdUnknownReturns404() throws Exception {
        when(taskService.getTaskById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found"));
    }

    @Test
    void createReturns201AndPassesRequestToService() throws Exception {
        when(taskService.createTask(any())).thenReturn(task(7, "Report"));

        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value(7))
                .andExpect(jsonPath("$.taskTitle").value("Report"));

        ArgumentCaptor<CreateTaskRequest> request = ArgumentCaptor.forClass(CreateTaskRequest.class);
        verify(taskService).createTask(request.capture());
        org.junit.jupiter.api.Assertions.assertEquals(
                new CreateTaskRequest(1, "Report", "d", 2, "doing", LocalDateTime.of(2026, 12, 1, 10, 0)),
                request.getValue());
    }

    @Test
    void createWithInvalidDataReturns400() throws Exception {
        when(taskService.createTask(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "taskTitle must be 1-200 characters"));

        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"projectId\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateReturnsUpdatedTask() throws Exception {
        when(taskService.updateTask(eq(3), any())).thenReturn(Optional.of(task(3, "Report")));

        mockMvc.perform(put("/api/tasks/3").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(3));
    }

    @Test
    void updateUnknownReturns404() throws Exception {
        when(taskService.updateTask(eq(999), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/999").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found"));
    }
}
