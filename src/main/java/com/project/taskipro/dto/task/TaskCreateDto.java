package com.project.taskipro.dto.task;

import com.project.taskipro.model.tasks.enums.StatusType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskCreateDto(String taskName, String taskDescription, LocalDateTime taskCreateDate, LocalDateTime taskFinishDate, StatusType statusType, Long deskId) {}
