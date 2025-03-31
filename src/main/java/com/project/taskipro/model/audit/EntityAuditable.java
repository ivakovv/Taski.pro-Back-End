package com.project.taskipro.model.audit;

import java.time.LocalDateTime;

public interface EntityAuditable {
    void setCreationDate(LocalDateTime creationDate);
}
