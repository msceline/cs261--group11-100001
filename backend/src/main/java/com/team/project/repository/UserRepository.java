package com.team.project.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team.project.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByTuId(String tuId);

    Optional<User> findByEmail(String email);

    boolean existsByTuId(String tuId);

    boolean existsByEmail(String email);

    /** Backward-compatible alias for UserService. */
    default Optional<User> findByStudentId(String studentId) {
        return findByTuId(studentId);
    }
}
