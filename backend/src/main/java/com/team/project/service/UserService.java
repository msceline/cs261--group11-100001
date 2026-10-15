package com.team.project.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.team.project.dto.user.UpdateProfileRequest;
import com.team.project.entity.User;
import com.team.project.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    public Optional<User> findById(int userId) {
        return userRepository.findById(userId);
    }

    /** Updates only the fields that are non-null in the request. */
    public Optional<User> updateProfile(int userId, UpdateProfileRequest req) {
        return userRepository.findById(userId).map(user -> {
            if (req.displayName() != null) user.setDisplayName(req.displayName().trim());
            if (req.email() != null) user.setEmail(req.email().trim());
            return userRepository.save(user);
        });
    }
}
