package com.team.project.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.team.project.entity.Role;
import com.team.project.entity.User;

/** Mock repository backed by in-memory data (not connected to SQL yet). */
@Repository
public class UserRepository {

    // password for all mock users is "password123" (SHA-256 hex)
    private static final String MOCK_PASSWORD_HASH =
            "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f";

    private final List<User> users = new CopyOnWriteArrayList<>(List.of(
            new User(1, "6609650001", MOCK_PASSWORD_HASH, "Somchai Jaidee", "somchai@dome.tu.ac.th",
                    LocalDateTime.of(2026, 1, 10, 9, 0), Role.STUDENT),
            new User(2, "6609650002", MOCK_PASSWORD_HASH, "Somying Rakdee", "somying@dome.tu.ac.th",
                    LocalDateTime.of(2026, 1, 11, 10, 30), Role.STUDENT),
            new User(3, "9900000001", MOCK_PASSWORD_HASH, "Visitor Guest", "visitor@example.com",
                    LocalDateTime.of(2026, 1, 12, 8, 0), Role.VISITOR)));

    public Optional<User> findByStudentId(String studentId) {
        return users.stream().filter(u -> u.getStudentId().equals(studentId)).findFirst();
    }

    public Optional<User> findById(int userId) {
        return users.stream().filter(u -> u.getUserId() == userId).findFirst();
    }

    /** Replaces the stored user that has the same id. */
    public User save(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == user.getUserId()) {
                users.set(i, user);
                return user;
            }
        }
        users.add(user);
        return user;
    }
}
