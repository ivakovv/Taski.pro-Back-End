package com.project.taskipro.model.audit;

import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

public class AuditListener {
    @PrePersist
    void preCreate(EntityAuditable auditable) {
        LocalDateTime now = LocalDateTime.now();
        auditable.setCreationDate(now);
    }
}
