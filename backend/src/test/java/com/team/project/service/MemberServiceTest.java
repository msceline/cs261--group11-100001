package com.team.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.team.project.dto.member.AddMemberRequest;
import com.team.project.dto.member.MemberResponse;
import com.team.project.dto.project.CreateProjectRequest;
import com.team.project.repository.MemberRepository;
import com.team.project.repository.ProjectRepository;
import com.team.project.repository.UserRepository;

/**
 * Mock data: project 1 (host user 1, member user 2), project 2 (host user 2).
 * User 3 is a global "visitor" account that is not in any project.
 */
class MemberServiceTest {

    private MemberService memberService;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        ProjectRepository projectRepository = new ProjectRepository();
        MemberRepository memberRepository = new MemberRepository();
        memberService = new MemberService(projectRepository, memberRepository, new UserService(new UserRepository()));
        projectService = new ProjectService(projectRepository, memberRepository);
    }

    private static HttpStatus statusOf(ResponseStatusException e) {
        return HttpStatus.valueOf(e.getStatusCode().value());
    }

    // ---------- list ----------

    @Test
    void listShowsMembersWithTheirRoleInProject() {
        List<MemberResponse> members = memberService.list(2, 1);

        assertEquals(List.of(1, 2), members.stream().map(MemberResponse::userId).toList());
        assertEquals(List.of("host", "member"), members.stream().map(MemberResponse::roleInProject).toList());
        assertEquals("6609650001", members.get(0).studentId());
    }

    @Test
    void listIsForbiddenForNonMember() {
        assertEquals(HttpStatus.FORBIDDEN,
                statusOf(assertThrows(ResponseStatusException.class, () -> memberService.list(1, 2))));
    }

    @Test
    void listUnknownProjectIsNotFound() {
        assertEquals(HttpStatus.NOT_FOUND,
                statusOf(assertThrows(ResponseStatusException.class, () -> memberService.list(1, 999))));
    }

    @Test
    void creatingAProjectAddsCallerAsHostMember() {
        int projectId = projectService.create(3, new CreateProjectRequest("New", null, null)).projectId();

        List<MemberResponse> members = memberService.list(3, projectId);

        assertEquals(1, members.size());
        assertEquals("host", members.get(0).roleInProject());
    }

    // ---------- add ----------

    @Test
    void hostCanAddMemberWithRole() {
        MemberResponse added = memberService.add(2, 2, new AddMemberRequest(1, "visitor"));

        assertEquals(1, added.userId());
        assertEquals(2, added.projectId());
        assertEquals("visitor", added.roleInProject());
        assertEquals(List.of(2, 1), memberService.list(1, 2).stream().map(MemberResponse::userId).toList());
    }

    @Test
    void roleDefaultsToMemberAndIsCaseInsensitive() {
        assertEquals("member", memberService.add(1, 1, new AddMemberRequest(3, null)).roleInProject());
        assertEquals("visitor", memberService.add(2, 2, new AddMemberRequest(3, " VISITOR ")).roleInProject());
    }

    @Test
    void roleHostOrUnknownRoleIsRejected() {
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> memberService.add(1, 1, new AddMemberRequest(3, "host")))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> memberService.add(1, 1, new AddMemberRequest(3, "admin")))));
    }

    @Test
    void nonHostCannotAddMember() {
        assertEquals(HttpStatus.FORBIDDEN, statusOf(assertThrows(ResponseStatusException.class,
                () -> memberService.add(2, 1, new AddMemberRequest(3, "member")))));
    }

    @Test
    void addRejectsUnknownUserDuplicateAndMissingId() {
        assertEquals(HttpStatus.NOT_FOUND, statusOf(assertThrows(ResponseStatusException.class,
                () -> memberService.add(1, 1, new AddMemberRequest(999, "member")))));
        assertEquals(HttpStatus.CONFLICT, statusOf(assertThrows(ResponseStatusException.class,
                () -> memberService.add(1, 1, new AddMemberRequest(2, "member")))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> memberService.add(1, 1, new AddMemberRequest(null, "member")))));
        assertEquals(HttpStatus.BAD_REQUEST, statusOf(assertThrows(ResponseStatusException.class,
                () -> memberService.add(1, 1, null))));
    }

    @Test
    void addToUnknownProjectIsNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, statusOf(assertThrows(ResponseStatusException.class,
                () -> memberService.add(1, 999, new AddMemberRequest(2, "member")))));
    }

    @Test
    void newlyAddedMemberCanSeeTheProject() {
        memberService.add(1, 1, new AddMemberRequest(3, "member"));

        assertEquals(1, projectService.get(3, 1).projectId());
    }
}
