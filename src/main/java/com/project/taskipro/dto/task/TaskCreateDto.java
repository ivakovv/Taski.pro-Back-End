package com.project.taskipro.dto.task;

import com.project.taskipro.model.tasks.enums.StatusType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskCreateDto(

        @NotNull
        @Size(min = 1, max = 75)
        String taskName,

        @Size(min = 1, max = 200)
        String taskDescription,

        @NotNull
        LocalDateTime taskCreateDate,

        LocalDateTime taskFinishDate,

        @NotNull
        @Enumerated(EnumType.STRING)
        StatusType statusType,

        Long deskId) {}
