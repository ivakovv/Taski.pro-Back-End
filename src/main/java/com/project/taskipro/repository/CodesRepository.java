package com.project.taskipro.repository;


import com.project.taskipro.model.codes.Code;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CodesRepository extends JpaRepository<Code, Long> {
    List<Code> getPasswordResetById(Long id);
    List<Code> getPasswordResetByUserId(Long userId);
    Optional<Code> findByCode(String resetCode);
    List<Code> getAllByCodeExpireTimeBefore(LocalDateTime currentDateTime);
}
