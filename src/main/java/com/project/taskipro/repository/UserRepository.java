package com.project.taskipro.repository;

import com.project.taskipro.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String newEmail);
}
