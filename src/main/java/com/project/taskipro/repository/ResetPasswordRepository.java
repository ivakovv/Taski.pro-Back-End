package com.project.taskipro.repository;


import com.project.taskipro.entity.PasswordReset;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface ResetPasswordRepository extends JpaRepository<PasswordReset, Long> {
    List<PasswordReset> getPasswordResetById(Long id);
    List<PasswordReset> getPasswordResetByUserId(Long userId);
    Optional<PasswordReset> findByResetCode(String resetCode);
    List<PasswordReset> getAllByResetCodeExpireTimeBefore(Calendar calendar);
}
