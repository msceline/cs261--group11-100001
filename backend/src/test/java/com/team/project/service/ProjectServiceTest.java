package com.team.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.team.project.dto.project.AddMemberRequest;
import com.team.project.dto.project.CreateProjectRequest;
import com.team.project.dto.project.ProjectResponse;
import com.team.project.repository.ProjectRepository;

/**
 * Mock data: project 1 (host 1, members 1 and 2), project 2 (host 2, member 2 only).
 * User 3 does not exist as a member anywhere.
 */
class ProjectServiceTest {

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(new ProjectRepository(), new UserService(new com.team.project.repository.UserRepository()));
    }

    private static HttpStatus statusOf(ResponseStatusException e) {
        return HttpStatus.valueOf(e.getStatusCode().value());
    }

    // ---------- create ----------

    @Test
    void createMakesCallerTheHostAndMember() {
        ProjectResponse created = projectService.create(3, new CreateProjectRequest("  My Group ", "desc", "CS300"));

        assertEquals("My Group", created.projectName());
        assertEquals(3, created.hostId());
        assertEquals(0, created.progress().compareTo(BigDecimal.ZERO));
        assertTrue(projectService.listForMember(3).stream().anyMatch(p -> p.projectId() == created.projectId()));
    }

    @Test
    void createAssignsNewIds() {
        ProjectResponse a = projectService.create(3, new CreateProjectRequest("A", null, null));
        ProjectResponse b = projectService.create(3, new CreateProjectRequest("B", null, null));

        assertTrue(a.projectId() != b.projectId());
    }

    @Test
    void createRejectsBlankOrMissingName() {
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.create(1, new CreateProjectRequest("   ", null, null)))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.create(1, new CreateProjectRequest(null, null, null)))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.create(1, null))));
    }

    @Test
    void createRejectsTooLongFields() {
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.create(1, new CreateProjectRequest("x".repeat(151), null, null)))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.create(1, new CreateProjectRequest("ok", "x".repeat(501), null)))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.create(1, new CreateProjectRequest("ok", null, "x".repeat(21))))));
    }

    @Test
    void createAcceptsFieldsAtMaxLength() {
        ProjectResponse created = projectService.create(1,
                new CreateProjectRequest("x".repeat(150), "y".repeat(500), "z".repeat(20)));

        assertEquals(150, created.projectName().length());
    }

    // ---------- list ----------

    @Test
    void listReturnsOnlyProjectsWhereUserIsMember() {
        List<ProjectResponse> forUser1 = projectService.listForMember(1);
        List<ProjectResponse> forUser2 = projectService.listForMember(2);

        assertEquals(List.of(1), forUser1.stream().map(ProjectResponse::projectId).toList());
        assertEquals(List.of(1, 2), forUser2.stream().map(ProjectResponse::projectId).toList());
        assertTrue(projectService.listForMember(3).isEmpty());
    }

    // ---------- get ----------

    @Test
    void getReturnsProjectForMember() {
        ProjectResponse project = projectService.get(2, 1);

        assertEquals(1, project.projectId());
        assertEquals(0, project.progress().compareTo(new BigDecimal("35.50")));
    }

    @Test
    void getIsForbiddenForNonMember() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> projectService.get(1, 2));

        assertEquals(HttpStatus.FORBIDDEN, statusOf(e));
    }

    @Test
    void getUnknownProjectIsNotFound() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> projectService.get(1, 999));

        assertEquals(HttpStatus.NOT_FOUND, statusOf(e));
    }

    // ---------- members ----------

    @Test
    void membersAreListedForMembers() {
        assertEquals(List.of(1, 2), projectService.listMembers(2, 1).stream().map(u -> u.userId()).toList());
    }

    @Test
    void membersCannotBeListedByNonMember() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> projectService.listMembers(1, 2));

        assertEquals(HttpStatus.FORBIDDEN, statusOf(e));
    }

    @Test
    void hostCanAddMember() {
        // project 2 has host 2 and no user 1
        var added = projectService.addMember(2, 2, new AddMemberRequest(1));

        assertEquals(1, added.userId());
        assertEquals(List.of(1, 2), projectService.listMembers(1, 2).stream().map(u -> u.userId()).toList());
    }

    @Test
    void nonHostCannotAddMember() {
        // user 2 is a member of project 1 but the host is user 1
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> projectService.addMember(2, 1, new AddMemberRequest(2)));

        assertEquals(HttpStatus.FORBIDDEN, statusOf(e));
    }

    @Test
    void addMemberRejectsUnknownUserDuplicateAndMissingId() {
        assertEquals(HttpStatus.NOT_FOUND, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.addMember(1, 1, new AddMemberRequest(999)))));
        assertEquals(HttpStatus.CONFLICT, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.addMember(1, 1, new AddMemberRequest(2)))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.addMember(1, 1, new AddMemberRequest(null)))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> projectService.addMember(1, 1, null))));
    }

    @Test
    void addMemberToUnknownProjectIsNotFound() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> projectService.addMember(1, 999, new AddMemberRequest(2)));

        assertEquals(HttpStatus.NOT_FOUND, statusOf(e));
    }
}
