package com.project.taskipro.dto.task;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponseDto(Long taskId, String taskName, String taskDescription, LocalDateTime taskCreateDate, LocalDateTime taskFinishDate) {}
