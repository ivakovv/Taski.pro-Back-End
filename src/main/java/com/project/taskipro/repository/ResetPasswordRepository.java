package com.project.taskipro.repository;


import com.project.taskipro.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordRepository extends JpaRepository<PasswordReset, Long> {
}
