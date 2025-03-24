package com.project.taskipro.dto.task;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskCreateDto(String taskName, String taskDescription, LocalDateTime taskCreateDate, LocalDateTime taskFinishDate, Long deskId) {}
